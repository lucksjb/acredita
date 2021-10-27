package br.com.acredita.authorizationserver.DTOout;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioAlteraSenhaDTO {
	@NotBlank
	private String senhaAtual;
	
	@NotBlank
	private String novaSenha;
}
