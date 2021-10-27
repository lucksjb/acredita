package br.com.acredita.customer.DTOout;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PersonEncryptedDTOout {
        private String cpf;
        private String dtNasc;
        private String nome;
        private EnderecoDTOout endereco;
        private List<DividaDTOout> dividas = new ArrayList<>();
        private String uid;


     
}
