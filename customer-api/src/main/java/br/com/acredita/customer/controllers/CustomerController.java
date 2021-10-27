package br.com.acredita.customer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.acredita.customer.config.RestControllerPath;
import br.com.acredita.customer.filters.PersonFilter;
import br.com.acredita.customer.services.CustomerService;

@RestController
@RequestMapping(RestControllerPath.Customer_PATH)
public class CustomerController implements ICustomerController {
    @Autowired
    private CustomerService service;

    @GetMapping("/consulta-customer-por-cpf")
    @Override
    // @PreAuthorize("hasRole('ROLE_Score.consultaScorePorCPF')")
    public ResponseEntity<Double> consultaScorePorCPF(@Validated PersonFilter filter)
    {
       return ResponseEntity.ok().body(service.consultaScorePorCPF(filter));
    }
    
}
