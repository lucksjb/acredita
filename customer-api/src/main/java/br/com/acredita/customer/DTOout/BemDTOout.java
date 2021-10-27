package br.com.acredita.customer.DTOout;

import br.com.acredita.customer.enums.TitpoBem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BemDTOout {
    private TitpoBem tipo;
    private Double valor;

}
