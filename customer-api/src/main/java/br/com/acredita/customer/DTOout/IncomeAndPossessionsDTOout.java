package br.com.acredita.customer.DTOout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.acredita.customer.enums.FonteRenda;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncomeAndPossessionsDTOout implements Serializable {
    private String uid;
    private int idade;
    private List<BemDTOout> bens = new ArrayList<>();
    private FonteRenda fonteRenda;
    private Double renda;

}
