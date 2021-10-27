package br.com.acredita.customer.DTOin;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CalculaScoreDTOin implements Serializable {
    private String uid;
    private int idade;
    private Double valorTotalBens;
    private Double valorRenda;
    private Double valorTotalDividas;
    private Double valorTotalMovFinaceira;



}
