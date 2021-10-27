package br.com.acredita.score.controllers;

import org.springframework.http.ResponseEntity;

import br.com.acredita.score.DTOin.CalculaScoreDTOin;

public interface IScoreController {
    public ResponseEntity<Double> calculaScore(CalculaScoreDTOin calculaScoreDTOin);
}
