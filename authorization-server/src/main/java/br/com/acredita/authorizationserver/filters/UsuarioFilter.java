package br.com.acredita.authorizationserver.filters;

import java.time.LocalDate;

import br.com.acredita.authorizationserver.enums.SimNao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UsuarioFilter  {
	private Long id;
	private String email;
	private String login;
	private String nome;
	private String fone;
	private LocalDate nascimentoINICIO;
	private LocalDate nascimentoFIM;
	private SimNao alterarSenhaProxLogin;
	private Long validadeDaSenha;
	private SimNao alterandoSenha;
	private String UUID;
}
