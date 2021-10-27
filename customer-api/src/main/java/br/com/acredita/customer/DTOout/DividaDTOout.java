package br.com.acredita.customer.DTOout;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DividaDTOout implements Serializable {
    private String credora;
    private Double valor;
}