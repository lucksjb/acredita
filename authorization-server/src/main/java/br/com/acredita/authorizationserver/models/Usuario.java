package br.com.acredita.authorizationserver.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.validators.DataNascimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "usuario")
public class Usuario extends BaseModel {
	@Column(name = "email", length = 100, nullable = false)
	@NotBlank
	@Email
	private String email;

	@Column(name = "login", length = 40, nullable = false)
	@NotBlank
	private String login;

	@Column(name = "nome", length = 100)
	@NotBlank
	private String nome;

	@Column(name = "fone", length = 14)
	private String fone;

	@Column(name = "senha", length = 60)
	@NotBlank
	private String senha;

	@Column(name = "nascimento")
	@NotNull
	@DataNascimento(idadeMinima = 13, idadeMaxima = 78)
	private LocalDate nascimento;

	@ManyToMany
	@JoinTable(name = "usuarioPerfil", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "perfil_id"))
	private List<Perfil> perfis = new ArrayList<>();

	@Column(name = "dataHoraSolicitouTrocaSenha")
	private LocalDateTime dataHoraSolicitouTrocaSenha;

	@Column(name = "uuidTrocaSenha", length = 100)
	private String uuidTrocaSenha;

	@Column(name = "alterarSenhaProxLogin", length = 1)
	private SimNao alterarSenhaProxLogin;

	@Column(name = "validadeDaSenha")
	private Long validadeDaSenha;

}
