package br.com.acredita.authorizationserver.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PerfilFilter {
	private Long id;
	private String descricao;
}
