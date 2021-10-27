package br.com.acredita.customer.controllers;

import org.springframework.http.ResponseEntity;

import br.com.acredita.customer.filters.PersonFilter;

public interface ICustomerController {
    public ResponseEntity<Double> consultaScorePorCPF(PersonFilter filter);
}
