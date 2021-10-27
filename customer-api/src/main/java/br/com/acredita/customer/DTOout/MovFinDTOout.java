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
public class MovFinDTOout implements Serializable {
    private String instituicao;
    private Double valorMov;
}