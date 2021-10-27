package br.com.acredita.customer.DTOout;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoDTOout implements Serializable {
    private String logradouro;
    private String cidade;
    private String uf;
    private String bairro;
    private String cep;
}
