package br.com.acredita.score.services;

import br.com.acredita.score.DTOin.CalculaScoreDTOin;

public interface IScoreService {
    public Double calculaScore(CalculaScoreDTOin calculaScoreDTOin);
}
