package br.com.acredita.authorizationserver.DTOout;

import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class UsuarioDTO {
	private Long id;
	
	@NotBlank
	private String email;
	private String login;
	private String nome;
	private String fone;
	private LocalDate nascimento;
	private List<PerfilDTO> perfis;

	public UsuarioDTO() {
	}

	public UsuarioDTO(Long id, String email, String login, String nome, String fone, LocalDate nascimento, List<PerfilDTO> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.login = login;
		this.nome = nome;
		this.fone = fone;
		this.nascimento = nascimento;
		this.perfis = perfis;
	}

	public UsuarioDTO(Long id, String email, String login, String nome, String fone, LocalDate nascimento) {
		super();
		this.id = id;
		this.email = email;
		this.login = login;
		this.nome = nome;
		this.fone = fone;
		this.nascimento = nascimento;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}


	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	public List<PerfilDTO> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<PerfilDTO> perfis) {
		this.perfis = perfis;
	}

}
