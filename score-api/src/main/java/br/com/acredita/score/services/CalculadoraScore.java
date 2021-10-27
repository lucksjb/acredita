package br.com.acredita.score.services;

import org.springframework.stereotype.Component;

import br.com.acredita.score.DTOin.CalculaScoreDTOin;

@Component
public class CalculadoraScore implements ICalculadoraScore {

    @Override
    public Double calculaScore(CalculaScoreDTOin calculaScoreDTOin) {
        Double score = 100D;

        if(calculaScoreDTOin.getIdade() < 25 ) score -= 10;
        if(calculaScoreDTOin.getValorRenda() > 2000D && calculaScoreDTOin.getValorRenda() < 3500D )  score += 5D;
        if(calculaScoreDTOin.getValorRenda() >= 3500D && calculaScoreDTOin.getValorRenda() < 7000D )  score += 10;
        if(calculaScoreDTOin.getValorRenda() >= 7000D)  score += 15;

        if(calculaScoreDTOin.getValorTotalBens() > 20000D && calculaScoreDTOin.getValorRenda() < 60000D )  score += 5D;
        if(calculaScoreDTOin.getValorRenda() >= 60000D && calculaScoreDTOin.getValorRenda() < 70000D )  score += 10;
        if(calculaScoreDTOin.getValorRenda() >= 70000D)  score += 15;

        if(calculaScoreDTOin.getValorTotalDividas() > 0 && calculaScoreDTOin.getValorTotalDividas() < 2000 )  score -= 5D;
        if(calculaScoreDTOin.getValorTotalDividas() >= 2000D && calculaScoreDTOin.getValorRenda() < 7000D )  score -= 10;
        if(calculaScoreDTOin.getValorTotalDividas() >= 70000D)  score -= 30;

        if(calculaScoreDTOin.getValorTotalMovFinaceira() > 0 && calculaScoreDTOin.getValorTotalMovFinaceira() < 2000 )  score += 5D;
        if(calculaScoreDTOin.getValorTotalMovFinaceira() >= 2000D && calculaScoreDTOin.getValorTotalMovFinaceira() < 7000D )  score += 10;
        if(calculaScoreDTOin.getValorTotalMovFinaceira() >= 70000D)  score += 20;

        if(score > 100 ) score = 100D;
        if(score < 0) score = 0D;

        return score;
    }
    
}
