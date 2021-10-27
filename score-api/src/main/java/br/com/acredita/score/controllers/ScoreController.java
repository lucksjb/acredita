package br.com.acredita.score.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acredita.score.DTOin.CalculaScoreDTOin;
import br.com.acredita.score.config.RestControllerPath;
import br.com.acredita.score.services.ScoreService;


@RestController
@RequestMapping(RestControllerPath.Score_PATH)
public class ScoreController implements IScoreController {
    @Autowired
    private ScoreService service;

    @PostMapping(value="/calcula-score")
    @Override
    public ResponseEntity<Double> calculaScore(@RequestBody @Valid  CalculaScoreDTOin calculaScoreDTOin) {
        return ResponseEntity.ok().body(service.calculaScore(calculaScoreDTOin) );
    }
    
}
