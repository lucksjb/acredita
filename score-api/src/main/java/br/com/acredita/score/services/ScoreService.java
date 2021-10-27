package br.com.acredita.score.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.acredita.score.DTOin.CalculaScoreDTOin;

@Service
public class ScoreService implements IScoreService {
    @Autowired
    private CalculadoraScore calculadoraScore;

    @Override
    public Double calculaScore(CalculaScoreDTOin calculaScoreDTOin) {
        return calculadoraScore.calculaScore(calculaScoreDTOin);
    }


}
