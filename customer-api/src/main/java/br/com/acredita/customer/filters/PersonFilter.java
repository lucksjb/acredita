package br.com.acredita.customer.filters;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PersonFilter implements Serializable {
    @NotEmpty
    @NotNull
    
    @Size(min = 11, max = 11,message = "o CPF tem de ter 11 digitos, com apenas numeros ")
    private String cpf;
    
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull
    private LocalDate dtNasc;

}
