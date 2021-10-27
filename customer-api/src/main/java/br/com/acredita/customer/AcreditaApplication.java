package br.com.acredita.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.acredita.customer.utils.EncryptDecrypt;


@SpringBootApplication
public class AcreditaApplication implements CommandLineRunner {
	@Autowired
	private ApplicationContext applicationContext;

	@Value("${other.api.people:http://localhost:81/people}")
	private String peopleBaseUrl;

	@Value("${other.api.income:http://localhost:81/income-and-possessions}")
	private String incomeAndPossessionsBaseUrl;

	@Value("${other.api.events:http://localhost:81/events}")
	private String eventsBaseUrl;

	@Value("${other.api.auth:http://localhost:8081/oauth/token}")
	private String authBaseUrl;

	@Value("${other.api.score:http://localhost:8082/api/private/scores}")
	private String scoreBaseUrl;

	@Bean
	public WebClient webClientPerson(WebClient.Builder builder) {
		return builder
			.baseUrl(peopleBaseUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();

	}

	@Bean
	public WebClient webClientIncomeAndPossessions(WebClient.Builder builder) {
		return builder
			.baseUrl(incomeAndPossessionsBaseUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();

	}
	
	
	@Bean
	public WebClient webClientEvent(WebClient.Builder builder) {
		return builder
			.baseUrl(eventsBaseUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();

	}

	@Bean
	public WebClient webClientAuthorizarionServer(WebClient.Builder builder) {
		return builder
			.baseUrl(authBaseUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
			.build();

	}

	@Bean
	public WebClient webClientScore(WebClient.Builder builder) {
		return builder
			.baseUrl(scoreBaseUrl)
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();

	}
	
	public static void main(String[] args) {
		SpringApplication.run(AcreditaApplication.class, args);
	}




	@Override
	public void run(String... args) throws Exception {

		EncryptDecrypt encryptDecrypt =  new EncryptDecrypt();
		
		System.out.println(encryptDecrypt.encrypt("50400131080","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1971-11-17","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("62331404046","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1994-12-17","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("28709899030","batatinha-frita1") +"   "+encryptDecrypt.encrypt("2000-08-17","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("35211394097","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1986-01-17","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("90115973036","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1964-03-22","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("47693719008","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1987-05-12","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("50904687023","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1995-10-15","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("28518300070","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1990-04-01","batatinha-frita1") );
		System.out.println(encryptDecrypt.encrypt("45824471045","batatinha-frita1") +"   "+encryptDecrypt.encrypt("1972-11-23","batatinha-frita1") );

		System.out.println("*-------------------- NOMES -----------------------------*");
		System.out.println(encryptDecrypt.encrypt("Mirella Moreira","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Calebe da Costa","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Gabrielly Ribeiro","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Esther Teixeira","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Yago Ferreira","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Calebe da Paz","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Maria Cecília da Cunha","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Vinicius Duarte","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Cauã da Costa","batatinha-frita1"));	
		
		System.out.println("*-------------------- LOGRADOUROS -----------------------------*");
		System.out.println(encryptDecrypt.encrypt("R. Dr. Gustavo Henrique da Paz, 127","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("Av. Laís Moraes, 576","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("Av. João Felipe da Rocha, 361","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("R. Luiz Henrique da Costa, 7820","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("R. Agatha Cardoso, 223A","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("R. Dr. Raul da Conceição, 132","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("Al. Maria Cecília Silva, s/n","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("R. Marcos Vinicius Pereira, 789","batatinha-frita1"));	
		System.out.println(encryptDecrypt.encrypt("Av. Isadora Nunes, 354","batatinha-frita1"));	


		System.out.println("*-------------------- CIDADES  -----------------------------*");
		System.out.println(encryptDecrypt.encrypt("Ribeirao preto","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Orlandia","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("Blumenau","batatinha-frita1"));


		System.out.println("*-------------------- UF  -----------------------------*");
		System.out.println(encryptDecrypt.encrypt("SP","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("SC","batatinha-frita1"));


		System.out.println("*-------------------- BAIRROS  -----------------------------*");
		System.out.println(encryptDecrypt.encrypt("CENTRO","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("CENTRO","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("CARDOSO","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("JOAO PAULO II","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("CANOINHA","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("VILA SEIXAS","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("VILA MARIANA","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("CENTRO","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("CENTRO","batatinha-frita1"));		


		System.out.println("*-------------------- CEP  -----------------------------*");
		System.out.println(encryptDecrypt.encrypt("14123-000","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("15354-000","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("75123-000","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("13156-000","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("45236-004","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("14670-121","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("16753-454","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("19756-123","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("18455-012","batatinha-frita1"));		

		System.out.println("*-------------------- EMPRESAS CREDORAS  -----------------------------*");
		System.out.println(encryptDecrypt.encrypt("MAGAZINE LUIZA","batatinha-frita1"));
		System.out.println(encryptDecrypt.encrypt("PONTO FRIO","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("CASAS BAHIA","batatinha-frita1"));		
		System.out.println(encryptDecrypt.encrypt("LOJAS CEM","batatinha-frita1"));		
		

/*
			{ "cpf": "50400131080", "dtnasc": "1971-11-17", "uid": "04ba9ccd-424a-420a-b4c0-ac4e7c91f557" },
			{ "cpf": "62331404046", "dtnasc": "1994-12-17", "uid": "8d98652d-060c-465c-b2bc-51eeca816d28" },
			{ "cpf": "28709899030", "dtnasc": "2000-08-17", "uid": "79071c9d-1e39-4ee1-aecd-07a7a9a8b4b3" },
			{ "cpf": "35211394097", "dtnasc": "1986-01-17", "uid": "c03cd246-57c5-4207-b9f8-a0f7e3b81f38" },
			{ "cpf": "90115973036", "dtnasc": "1964-03-22", "uid": "9d917d1f-d539-4aba-b241-61f3a45f186d" },
			{ "cpf": "47693719008", "dtnasc": "1987-05-12", "uid": "a17011ca-8c26-4fba-81b1-ecbe1c060797" },
			{ "cpf": "50904687023", "dtnasc": "1995-10-15", "uid": "89431a5c-3404-11ec-8d3d-0242ac130003" },
			{ "cpf": "28518300070", "dtnasc": "1990-04-01", "uid": "6142ab1f-297f-46b5-87d6-0b090844629a" },
			{ "cpf": "45824471045", "dtnasc": "1972-11-23", "uid": "97e85ffe-8b1a-4bff-9899-b92c7c8e7792" }
		*/
		
		

		// ObtemTokenService obtemTokenService = applicationContext.getBean(ObtemTokenService.class);
	
	
		// String token = obtemTokenService.obtemToken("admin", "123", "any", "canela-seca").getBody();
		// System.out.println("Token: === "+ token);

		
	}

}
