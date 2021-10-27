package br.com.acredita.authorizationserver.DTOin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.models.Perfil;
import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.repositories.PerfilRepository;
import br.com.acredita.authorizationserver.validators.DataNascimento;
import br.com.acredita.authorizationserver.validators.TamanhoMinimoLista;

public class UsuarioDTOinCreate  {
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String login;

	@NotBlank
	private String nome;
	
	private String fone;
	private String senha;

	@NotNull
	@DataNascimento(idadeMinima=13, idadeMaxima=78)
	private LocalDate nascimento;
	private List<Long> perfis = new ArrayList<>();
	private SimNao alterarSenhaProxLogin;
	private Long validadeDaSenha;

	public UsuarioDTOinCreate() {
	}

	public UsuarioDTOinCreate(String email, String login, String nome, String fone, String senha, LocalDate nascimento, List<Long> perfis, SimNao alterarSenhaProxLogin,
			Long validadeDaSenha) {
		super();
		this.email = email;
		this.login = login;
		this.nome = nome;
		this.fone = fone;
		this.senha = senha;
		this.nascimento = nascimento;
		this.perfis = perfis;
		this.alterarSenhaProxLogin = alterarSenhaProxLogin;
		this.validadeDaSenha = validadeDaSenha;
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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

	@TamanhoMinimoLista(tamanhoMinimo = 1, message = "Tem de ter no minimo 1 perfil")
	public List<Long> getPerfis() {
		return perfis;
	}

	public void setPerfis(List<Long> perfis) {
		this.perfis = perfis;
	}

	public SimNao getAlterarSenhaProxLogin() {
		return alterarSenhaProxLogin;
	}

	public void setAlterarSenhaProxLogin(SimNao alterarSenhaProxLogin) {
		this.alterarSenhaProxLogin = alterarSenhaProxLogin;
	}

	public Long getValidadeDaSenha() {
		return validadeDaSenha;
	}

	public void setValidadeDaSenha(Long validadeDaSenha) {
		this.validadeDaSenha = validadeDaSenha;
	}

	public Usuario create(PerfilRepository perfilRepository) {
		PropertyMap<UsuarioDTOinCreate, Usuario> propertyMap = new PropertyMap<UsuarioDTOinCreate, Usuario>() {
			@Override
			protected void configure() {
				skip(destination.getId());
				skip(destination.getPerfis());
			}
		};

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(propertyMap);
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

		Usuario usuario = new Usuario();
		usuario.setId(0L);
		modelMapper.map(this, usuario);

		if (this.perfis != null) {
			usuario.getPerfis().clear();
			List<Perfil> perfilList = new ArrayList<>();
			this.perfis.forEach(item -> {
				Optional<Perfil> perfil = perfilRepository.findById(item);
				if (perfil.isPresent()) {
					perfilList.add(perfil.get());
				}
			});
			usuario.setPerfis(perfilList);
		}

		return usuario;
	}

}
