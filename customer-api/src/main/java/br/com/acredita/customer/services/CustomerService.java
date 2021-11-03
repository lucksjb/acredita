package br.com.acredita.customer.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.acredita.customer.DTOin.CalculaScoreDTOin;
import br.com.acredita.customer.DTOout.EventDTOout;
import br.com.acredita.customer.DTOout.IncomeAndPossessionsDTOout;
import br.com.acredita.customer.DTOout.PersonEncryptedDTOout;
import br.com.acredita.customer.config.CacheConfiguration;
import br.com.acredita.customer.exceptions.entity.PessoaEncontradaException;
import br.com.acredita.customer.filters.PersonFilter;
import br.com.acredita.customer.utils.IEncryptDecrypt;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService implements ICustomerService {
    @Autowired
    private LogService logService;

    @Autowired
    private IEncryptDecrypt encryptDecrypt;

    @Autowired
    private WebClient webClientPerson;

    @Autowired
    private WebClient webClientIncomeAndPossessions;

    @Autowired
    private WebClient webClientEvent;

    @Autowired
    private WebClient webClientScore;

    @Value("${decrypt.password:batatinha-frita1}")
    private String decryptPassword;

    @Override
    @Cacheable(CacheConfiguration.CUSTOMER)
    public Double consultaScorePorCPF(PersonFilter filter) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dtNasc = filter.getDtNasc().format(formatter);

        String cpfEnc = encryptDecrypt.encrypt(filter.getCpf(), decryptPassword);
        String dtNascEnc = encryptDecrypt.encrypt(dtNasc, decryptPassword);


        Flux<PersonEncryptedDTOout> fluxPerson = webClientPerson.method(HttpMethod.GET)
                .uri("?cpf={cpf}&dtNasc={dtNasc}", cpfEnc, dtNascEnc)
                .retrieve()
                .bodyToFlux(PersonEncryptedDTOout.class);

        PersonEncryptedDTOout personEncrypted = fluxPerson.blockFirst();
        if(personEncrypted == null) {
            throw new PessoaEncontradaException(filter.getCpf(), filter.getDtNasc());
        }

        Flux<IncomeAndPossessionsDTOout> fluxIncomeAndPossessions = webClientIncomeAndPossessions.method(HttpMethod.GET)
                .uri("?uid={uid}", personEncrypted.getUid())
                .retrieve()
                .bodyToFlux(IncomeAndPossessionsDTOout.class);

        Flux<EventDTOout> fluxEvent = webClientEvent.method(HttpMethod.GET)
                .uri("?uid={uid}", personEncrypted.getUid())
                .retrieve()
                .bodyToFlux(EventDTOout.class);

        IncomeAndPossessionsDTOout incomeAndPossessions = fluxIncomeAndPossessions.blockFirst();
        EventDTOout event = fluxEvent.blockFirst();

        
        LocalDate nasc = LocalDate.parse(encryptDecrypt.decrypt(personEncrypted.getDtNasc(), decryptPassword));

        int idade = Period.between(
            nasc,
            LocalDate.now()).getYears();
        
        CalculaScoreDTOin calculaScoreDTOin = CalculaScoreDTOin.builder()
            .uid(personEncrypted.getUid())
            .idade( idade )
            .valorTotalBens(incomeAndPossessions.getBens().stream().mapToDouble(b-> b.getValor()).reduce((ant,valor) -> ant+valor).orElseGet(() -> 0D) )
            .valorRenda(incomeAndPossessions.getRenda())
            .valorTotalDividas(personEncrypted.getDividas().stream().mapToDouble(d -> d.getValor()).reduce((ant,valor) -> ant+valor ).orElseGet(() -> 0D) )
            .valorTotalMovFinaceira(event.getMovfinan().stream().mapToDouble(mf -> mf.getValorMov()).reduce((ant,valor) -> ant+valor).orElseGet(() -> 0D) )
            .build();

        System.out.println("===================== "+calculaScoreDTOin);
        
        Mono<Double> fluxCalculaScore = webClientScore.method(HttpMethod.POST)
            .uri("/calcula-score")
            .bodyValue(calculaScoreDTOin)
            .retrieve()
            .bodyToMono(Double.class);
         Double score = fluxCalculaScore.block();
         logService.sendMessage("logando "+personEncrypted.getUid()+"  score "+score);
        return score; 
    }

}
