package br.com.acredita.score.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.acredita.score.DTOin.CalculaScoreDTOin;

@Service
public class ScoreService implements IScoreService {
    private static final Logger log = LoggerFactory.getLogger(ScoreService.class);
    
    private static String applicationName = "Score-api";

    @Autowired
    private CalculadoraScore calculadoraScore;

    @Override
    public Double calculaScore(CalculaScoreDTOin calculaScoreDTOin) {
        log.info("calcula score ", applicationName);
        return calculadoraScore.calculaScore(calculaScoreDTOin);
    }


}
