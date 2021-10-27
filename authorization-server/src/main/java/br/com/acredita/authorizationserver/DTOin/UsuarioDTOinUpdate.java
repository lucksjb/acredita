package br.com.acredita.authorizationserver.DTOin;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.models.Perfil;
import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.repositories.PerfilRepository;

public class UsuarioDTOinUpdate {
	private String email;
	private String login;
	private String nome;
	private String fone;
	private LocalDate nascimento;
	private List<Long> perfis = new ArrayList<>();
	private SimNao alterarSenhaProxLogin;
	private Long validadeDaSenha;

	public UsuarioDTOinUpdate() {
	}

	public UsuarioDTOinUpdate(String email, String login, String nome, String fone, LocalDate nascimento,
			List<Long> perfis, SimNao alterarSenhaProxLogin, Long validadeDaSenha) {
		super();
		this.email = email;
		this.login = login;
		this.nome = nome;
		this.fone = fone;
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

	public LocalDate getNascimento() {
		return nascimento;
	}

	public void setNascimento(LocalDate nascimento) {
		this.nascimento = nascimento;
	}

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

	public Usuario update(Usuario usuario, PerfilRepository perfilRepository) {
		PropertyMap<UsuarioDTOinUpdate, Usuario> propertyMap = new PropertyMap<UsuarioDTOinUpdate, Usuario>() {
			@Override
			protected void configure() {
				skip(destination.getId());
				skip(destination.getPerfis());
				skip(destination.getSenha());
			}
		};

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(propertyMap);
		modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

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
