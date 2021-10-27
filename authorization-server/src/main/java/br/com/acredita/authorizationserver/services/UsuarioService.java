package br.com.acredita.authorizationserver.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.acredita.authorizationserver.DTOin.UsuarioDTOinCreate;
import br.com.acredita.authorizationserver.DTOin.UsuarioDTOinUpdate;
import br.com.acredita.authorizationserver.DTOout.UsuarioAlteraSenhaDTO;
import br.com.acredita.authorizationserver.DTOout.UsuarioDTO;
import br.com.acredita.authorizationserver.config.security.SegurancaProperties;
import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.exceptions.NegocioException;
import br.com.acredita.authorizationserver.filters.UsuarioFilter;
import br.com.acredita.authorizationserver.freemarker.FMTemplate;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.repositories.PerfilRepository;
import br.com.acredita.authorizationserver.repositories.UsuarioRepository;
import br.com.acredita.authorizationserver.utils.java.JavaFunctions;
import br.com.acredita.authorizationserver.utils.java.email.EnvioEmailService;
import br.com.acredita.authorizationserver.utils.java.email.EnvioEmailService.Mensagem;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilRepository perfilRepository;

	@Autowired
	private EnvioEmailService envioEmail;

	@Autowired
	private SegurancaProperties properties;

	@Autowired
	private FMTemplate fmTemplate;

	public Page<UsuarioDTO> findByFilter(Pageable pageable, UsuarioFilter filter) {
		return usuarioRepository.findByFilter(pageable, filter);
	}

	public List<UsuarioDTO> findToCombobox(UsuarioFilter filter) {
		return usuarioRepository.findToCombobox(filter);
	}

	public Optional<Usuario> findById(Long id) {
		return usuarioRepository.findById(id);
	}

	public List<Usuario> findToReport(UsuarioFilter filter) {
		return usuarioRepository.findToReport(filter);
	}

	public Usuario create(UsuarioDTOinCreate usuarioRequestDTO) {
		if (usuarioRequestDTO.getSenha() == null) {
			usuarioRequestDTO.setSenha(properties.getNovoUsuarioSenhaPadrao());
		}

		if (properties.getNovoUsuarioExigeSenhaForte() == SimNao.SIM && !JavaFunctions.senhaForte(usuarioRequestDTO.getSenha())) {
			throw new NegocioException("Senha fraca e sistema de segurança configurado para senha forte");
		}

		if (properties.getSenhaEncoded() == SimNao.SIM) {
			usuarioRequestDTO.setSenha(passwordEncoder.encode(usuarioRequestDTO.getSenha()));
		}

		if (usuarioRepository.findByEmail(usuarioRequestDTO.getEmail()).isPresent()) {
			throw new NegocioException("Email já pertence a outro usuário ...");
		}

		if (usuarioRepository.findByLogin(usuarioRequestDTO.getLogin()).isPresent()) {
			String sugestao = usuarioRepository.criaLoginValido(usuarioRequestDTO.getNome(), usuarioRequestDTO.getNascimento());
			throw new NegocioException("Login já pertence a outro usuário ...  " + "sugerido o login : '" + sugestao + "'");
		}

		Usuario usuario = usuarioRequestDTO.create(perfilRepository);
		usuario.setAlterarSenhaProxLogin(properties.getNovoUsuarioAlteraSenhaProxLogin());
		usuario.setValidadeDaSenha(properties.getNovoUsuarioValidadeSenhaDias());
		return usuarioRepository.save(usuario);
	}

	public Usuario update(Long id, UsuarioDTOinUpdate usuarioRequestDTO) {
		Optional<Usuario> salvo = usuarioRepository.findById(id);
		if (!salvo.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}

		Usuario usuario = usuarioRequestDTO.update(salvo.get(), perfilRepository);

		return usuarioRepository.save(usuario);

	}

	public void delete(Long id) {
		usuarioRepository.deleteById(id);
	}

	// ************************* OUTROS METODOS ************* //

	public Optional<UsuarioDTO> findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	public Optional<UsuarioDTO> findByLogin(String login) {
		return usuarioRepository.findByLogin(login);
	}

	public Optional<UsuarioDTO> findByUUID(String uUID) {
		return usuarioRepository.findByUUID(uUID);
	}

	public Optional<UsuarioDTO> updateSenha(Long id, String novaSenha) {
		novaSenha = JavaFunctions.md5(id.toString().trim() + novaSenha.toString());
		return usuarioRepository.updateSenha(id, novaSenha);
	}

	public String loginValido(String login) {
		return usuarioRepository.loginValido(login);
	}

	public String criaLoginValido(String nome, LocalDate dataNascimento) {
		return usuarioRepository.criaLoginValido(nome, dataNascimento);
	}

	public Usuario trocaSenha(UsuarioAlteraSenhaDTO usuarioDTO) {
		return usuarioRepository.trocaSenha(usuarioDTO);
	}

	public List<MenuModel> menuDoUsuario(Long id) {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isPresent()) {
			return usuarioRepository.menuDoUsuario(usuario.get());
		} else
			return new ArrayList<MenuModel>();
	}

	public Optional<Usuario> findFirstByEmail(String email) {
		return usuarioRepository.findFirstByEmail(email);
	}

	public Optional<Usuario> findFirstByLogin(String login) {
		return usuarioRepository.findFirstByLogin(login);
	}

	// https://www.algaworks.com/aulas/2081/configurando-o-projeto-para-envio-de-e-mails-usando-servidor-smtp/
	// https://app.sendgrid.com/
	public void solicitaResetDeSenha(String loginEmail) {
		Optional<UsuarioDTO> usuario;
		if (loginEmail.contains("@")) {
			usuario = usuarioRepository.findByEmail(loginEmail);
		} else {
			usuario = usuarioRepository.findByLogin(loginEmail);
		}

		if (!usuario.isPresent()) {
			throw new NegocioException("Usuário não cadastrado ");
		}

		Optional<Usuario> user = findById(usuario.get().getId());
		if (!user.isPresent()) {
			throw new NegocioException("Usuário não cadastrado ");
		}

		user.get().setUuidTrocaSenha(JavaFunctions.uuid());
		LocalDateTime agora = LocalDateTime.now();
		user.get().setDataHoraSolicitouTrocaSenha(agora);
		usuarioRepository.save(user.get());

		LocalDateTime expira = agora.plusMinutes(properties.getTempoEmMinutosExpiraLinkResetSenha());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

		Mensagem mensagem = Mensagem.builder()
				.assunto("Solicitação de reset de senha para o aCredita !!!")
				.destinatario(user.get().getEmail())
				.template("solicita-reset-de-senha.html")
				.variavel("usuarioNome", user.get().getNome())
				.variavel("uuid", user.get().getUuidTrocaSenha())
				.variavel("linkExpira", expira.format(formatter))
				.build();

		envioEmail.enviar(mensagem);
	}

	public String resetarSenha(String uuid) {
		Optional<UsuarioDTO> userDTO = usuarioRepository.findByUUID(uuid);
		if (!userDTO.isPresent()) {
			String html = fmTemplate.templateName("link-de-reset-invalido.html")
					.build().processarTemplate();
			return html;
		}
		
		Optional<Usuario> user = usuarioRepository.findById(userDTO.get().getId());
		if (!user.isPresent()) {
			String html = fmTemplate.templateName("link-de-reset-invalido.html")
					.build().processarTemplate();
			return html;
		}
		
		Long tempoSolicitacao = Duration.between(user.get().getDataHoraSolicitouTrocaSenha(), LocalDateTime.now()).toMinutes();
		
		// checa se ja expirou
		if (tempoSolicitacao > properties.getTempoEmMinutosExpiraLinkResetSenha()) {
			String nomeUsuario = user.get().getNome();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String solicitou = user.get().getDataHoraSolicitouTrocaSenha().format(formatter);
			
			String html = fmTemplate.templateName("link-de-reset-expirado.html")
					.variavel("usuario", nomeUsuario)
					.variavel("dataHoraSolicitouTrocaSenha", solicitou)
					.build().processarTemplate();
			
			return html;
		}


		String novaSenha;
		if (properties.getSenhaDeResetPadrao() == SimNao.SIM) {
			novaSenha = properties.getNovoUsuarioSenhaPadrao();
		} else {
			novaSenha = JavaFunctions.uuid().substring(1, 5);
		}
		
		
		user.get().setAlterarSenhaProxLogin(SimNao.SIM);
		user.get().setUuidTrocaSenha(null);
		user.get().setDataHoraSolicitouTrocaSenha(null);
		if(properties.getSenhaEncoded() == SimNao.SIM) {
			user.get().setSenha(passwordEncoder.encode(novaSenha));
		} else {
			user.get().setSenha(novaSenha);
		}
		usuarioRepository.save(user.get());

		String html = fmTemplate.templateName("senha-alterada-com-sucesso.html")
				.variavel("usuario", user.get().getNome())
				.variavel("novaSenha", novaSenha)
				.build()
				.processarTemplate();
		return html;
	}
}
