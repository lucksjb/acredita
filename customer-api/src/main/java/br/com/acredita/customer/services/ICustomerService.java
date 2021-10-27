package br.com.acredita.customer.services;

import br.com.acredita.customer.filters.PersonFilter;

public interface ICustomerService {
    public Double consultaScorePorCPF(PersonFilter filter);
}
