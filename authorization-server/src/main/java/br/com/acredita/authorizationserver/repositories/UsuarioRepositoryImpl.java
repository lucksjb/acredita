package br.com.acredita.authorizationserver.repositories;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.acredita.authorizationserver.DTOout.UsuarioAlteraSenhaDTO;
import br.com.acredita.authorizationserver.DTOout.UsuarioDTO;
import br.com.acredita.authorizationserver.config.security.SegurancaProperties;
import br.com.acredita.authorizationserver.enums.SimNao;
import br.com.acredita.authorizationserver.enums.TipoItemMenu;
import br.com.acredita.authorizationserver.exceptions.NegocioException;
import br.com.acredita.authorizationserver.filters.UsuarioFilter;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Programa;
import br.com.acredita.authorizationserver.models.Usuario;
import br.com.acredita.authorizationserver.utils.java.JavaFunctions;


public class UsuarioRepositoryImpl implements UsuarioRepositoryQuery {
	@Autowired
	@Lazy
	private UsuarioRepository usuarioRepository;

	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SegurancaProperties properties;

	@Autowired
	@Lazy
	private ProgramaRepository programaRepository;

	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<UsuarioDTO> findByFilter(Pageable pageable, UsuarioFilter usuarioFilter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UsuarioDTO> criteriaQuery = cb.createQuery(UsuarioDTO.class);
		Root<Usuario> rootFrom = criteriaQuery.from(Usuario.class);

		AdicionaCampos(cb, criteriaQuery, rootFrom);

		criteriaQuery.where(criaRestricoes(usuarioFilter, cb, rootFrom));
		criteriaQuery.orderBy(cb.asc(rootFrom.get("nome")));

		TypedQuery<UsuarioDTO> query = manager.createQuery(criteriaQuery);
		adicionarRestricoesDePaginacao(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, total(usuarioFilter));
	}

	private Long total(UsuarioFilter usuarioFilter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Usuario> rootFrom = cq.from(Usuario.class);
		cq.where(criaRestricoes(usuarioFilter, cb, rootFrom));
		cq.select(cb.count(rootFrom));
		return manager.createQuery(cq).getSingleResult();
	}

	@Override
	public List<UsuarioDTO> findToCombobox(UsuarioFilter usuarioFilter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UsuarioDTO> criteriaQuery = cb.createQuery(UsuarioDTO.class);
		Root<Usuario> rootFrom = criteriaQuery.from(Usuario.class);

		AdicionaCampos(cb, criteriaQuery, rootFrom);

		criteriaQuery.where(criaRestricoes(usuarioFilter, cb, rootFrom));
		criteriaQuery.orderBy(cb.asc(rootFrom.get("nome")));

		TypedQuery<UsuarioDTO> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	public List<Usuario> findToReport(UsuarioFilter filter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = cb.createQuery(Usuario.class);
		Root<Usuario> rootFrom = criteriaQuery.from(Usuario.class);

		criteriaQuery.select(rootFrom); // select all

		criteriaQuery.where(criaRestricoes(filter, cb, rootFrom));
		criteriaQuery.orderBy(cb.asc(rootFrom.get("nome")));

		TypedQuery<Usuario> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	public Optional<UsuarioDTO> findByEmail(String email) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UsuarioDTO> criteriaQuery = cb.createQuery(UsuarioDTO.class);
		Root<Usuario> rootFrom = criteriaQuery.from(Usuario.class);

		AdicionaCampos(cb, criteriaQuery, rootFrom);
		criteriaQuery.where(cb.equal(rootFrom.get("email"), email.trim()));

		TypedQuery<UsuarioDTO> query = manager.createQuery(criteriaQuery);
		query.setMaxResults(1); // top 1
		try {
			return Optional.of(query.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<UsuarioDTO> findByLogin(String login) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UsuarioDTO> criteriaQuery = cb.createQuery(UsuarioDTO.class);
		Root<Usuario> rootFrom = criteriaQuery.from(Usuario.class);

		AdicionaCampos(cb, criteriaQuery, rootFrom);
		criteriaQuery.where(cb.equal(rootFrom.get("login"), login.trim()));

		TypedQuery<UsuarioDTO> query = manager.createQuery(criteriaQuery);
		query.setMaxResults(1); // top 1

		try {
			return Optional.of(query.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public Optional<UsuarioDTO> findByUUID(String UUID) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<UsuarioDTO> criteriaQuery = cb.createQuery(UsuarioDTO.class);
		Root<Usuario> rootFrom = criteriaQuery.from(Usuario.class);

		AdicionaCampos(cb, criteriaQuery, rootFrom);
		criteriaQuery.where(cb.equal(rootFrom.get("UUID"), UUID.trim()));

		TypedQuery<UsuarioDTO> query = manager.createQuery(criteriaQuery);
		query.setMaxResults(1); // top 1
		
		try {
			return Optional.of(query.getSingleResult());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	// methods de apoio
	private void AdicionaCampos(CriteriaBuilder cb, CriteriaQuery<UsuarioDTO> criteriaQuery, Root<Usuario> rootFrom) {
		criteriaQuery.select(cb.construct(UsuarioDTO.class, 
				rootFrom.get("id"), 
				rootFrom.get("email"), 
				rootFrom.get("login"), 
				rootFrom.get("nome"), 
				rootFrom.get("fone"),
				rootFrom.get("nascimento")
				));
	}

	private Predicate[] criaRestricoes(UsuarioFilter usuarioFilter, CriteriaBuilder cb, Root<Usuario> rootFrom) {
		List<Predicate> predicates = new ArrayList<>();
		Predicate condition = null;
		if (usuarioFilter.getId() != null && usuarioFilter.getId() != 0) {
			condition = cb.equal(rootFrom.get("id"), usuarioFilter.getId());
			predicates.add(condition);
		}

		if (!ObjectUtils.isEmpty(usuarioFilter.getEmail())) {
			condition = cb.like(rootFrom.get("email"), usuarioFilter.getEmail().trim() + "%");
			predicates.add(condition);
		}

		if (!ObjectUtils.isEmpty(usuarioFilter.getLogin())) {
			condition = cb.like(rootFrom.get("login"), usuarioFilter.getLogin().trim() + "%");
			predicates.add(condition);
		}

		if (!ObjectUtils.isEmpty(usuarioFilter.getNome())) {
			condition = cb.like(rootFrom.get("nome"), usuarioFilter.getNome().trim() + "%");
			predicates.add(condition);
		}

		if (!ObjectUtils.isEmpty(usuarioFilter.getFone())) {
			condition = cb.like(rootFrom.get("fone"), "%" + usuarioFilter.getFone().trim() + "%");
			predicates.add(condition);
		}

		if (usuarioFilter.getNascimentoINICIO() != null) {
			condition = cb.greaterThanOrEqualTo(rootFrom.get("nascimento"), usuarioFilter.getNascimentoINICIO());
			predicates.add(condition);
		}

		if (usuarioFilter.getNascimentoFIM() != null) {
			condition = cb.lessThanOrEqualTo(rootFrom.get("nascimento"), usuarioFilter.getNascimentoFIM());
			predicates.add(condition);
		}
		
		if (usuarioFilter.getAlterandoSenha() != null) {
			condition = cb.lessThan(rootFrom.get("dataHoraSolicitouTrocaSenha"), LocalDate.now());
			predicates.add(condition);
		}

		if (usuarioFilter.getAlterarSenhaProxLogin() != null) {
			condition = cb.equal(rootFrom.get("alterarSenhaProxLogin"), usuarioFilter.getAlterarSenhaProxLogin());
			predicates.add(condition);
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	// methodo de update
	@Override
	public Optional<UsuarioDTO> updateSenha(Long id, String novaSenha) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaUpdate<Usuario> criteriaUpdate = cb.createCriteriaUpdate(Usuario.class);
		Root<Usuario> rootFrom = criteriaUpdate.from(Usuario.class);
		criteriaUpdate.set("senha", novaSenha);

		criteriaUpdate.where(cb.equal(rootFrom.get("id"), id));
		manager.createQuery(criteriaUpdate);

		Usuario usuario = usuarioRepository.findById(id).get();
		UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getId(), usuario.getEmail(), usuario.getLogin(), usuario.getNome(), usuario.getFone(), usuario.getNascimento());

		return Optional.of(usuarioDTO);
	}

	@Override
	public String loginValido(String login) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Usuario> root = cq.from(Usuario.class);
		cq.select(cb.construct(Long.class, root.get("id")));
		Optional<Long> id;

		Random geraProximo = new Random();
		do {
			cq.where(cb.equal(root.get("login"), login));
			TypedQuery<Long> query = manager.createQuery(cq);

			try {

				id = Optional.of(query.getSingleResult());
			} catch (Exception e) {
				id = Optional.empty();
			}

			if (id.isPresent()) {
				login = login.trim() + geraProximo.nextInt(100);
			}
		} while (id.isPresent());

		return login;
	}

	@Override
	public String criaLoginValido(String nome, LocalDate dataNascimento) {
		if(nome == null) {
			throw new NegocioException("Obrigatório informar o nome");
		}
		
		String[] palavras = nome.split(" ");
		if(palavras.length <= 1) {
			throw new NegocioException("Nome tem de ter ao menos duas palavras");
		}
		
		nome = palavras[0];
		String sobreNome = palavras[palavras.length-1];
		
		if(dataNascimento == null) {
			dataNascimento = LocalDate.of(1971, 11, 17);
		}
		
		
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Usuario> root = cq.from(Usuario.class);

		TypedQuery<Long> query;
		Optional<Long> id;
		String login;

		// Verifica o Nome
		login = nome.trim().toLowerCase();
		cq.select(cb.construct(Long.class, root.get("id")));
		cq.where(cb.equal(root.get("login"), login));
		query = manager.createQuery(cq);
		try {

			id = Optional.of(query.getSingleResult());
		} catch (Exception e) {
			id = Optional.empty();
		}
		if (!id.isPresent()) {
			return login;
		}

		// Verifica o Nome + SobreNome
		cq.select(cb.construct(Long.class, root.get("id")));
		login = nome.trim().toLowerCase() + "." + sobreNome.trim().toLowerCase();
		cq.where(cb.equal(root.get("login"), login));
		query = manager.createQuery(cq);
		try {

			id = Optional.of(query.getSingleResult());
		} catch (Exception e) {
			id = Optional.empty();
		}
		if (!id.isPresent()) {
			return login;
		}

		// Verifica o Nome + SobreNome + idade
		cq.select(cb.construct(Long.class, root.get("id")));
		
		login = nome.trim().toLowerCase() + "." + sobreNome.trim().toLowerCase() + JavaFunctions.idade(dataNascimento);
		cq.where(cb.equal(root.get("login"), login));
		query = manager.createQuery(cq);
		try {

			id = Optional.of(query.getSingleResult());
		} catch (Exception e) {
			id = Optional.empty();
		}
		if (!id.isPresent()) {
			return login;
		}

		return loginValido(login);
	}

	@Override
	public Usuario trocaSenha(UsuarioAlteraSenhaDTO usuarioDTO) {
		String senhaAtual = usuarioDTO.getSenhaAtual();
		String novaSenha = usuarioDTO.getNovaSenha();

		if (senhaAtual.equals(novaSenha)) {
			throw new NegocioException("Nova senha tem de ser diferente da Senha atual");
		}

		if (properties.getNovoUsuarioExigeSenhaForte() == SimNao.SIM && !JavaFunctions.senhaForte(novaSenha)) {
			throw new NegocioException("Senha fraca e sistema de segurança configurado para senha forte");
		}

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();

		Usuario usuario = usuarioRepository.findFirstByLogin(currentPrincipalName).orElseThrow(() -> new UsernameNotFoundException("Usuário não existe"));

		if (properties.getSenhaEncoded() == SimNao.SIM) {
			if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
				throw new NegocioException("Senha antiga não confere");
			}
			novaSenha = passwordEncoder.encode(novaSenha);

		} else if (properties.getSenhaEncoded() == SimNao.NAO) {
			if (usuario.getSenha() != senhaAtual) {
				throw new NegocioException("Senha antiga não confere");
			}
		}
		usuario.setAlterarSenhaProxLogin(SimNao.NAO);
		usuario.setSenha(novaSenha);
		return usuarioRepository.save(usuario);
	}

	@Override
	public List<MenuModel> menuDoUsuario(Usuario usuario) {
		// cria lista das permissoes "aglutinadas" de varios perfis
		List<Programa> permissoes = new ArrayList<>();
		usuario.getPerfis().forEach(perfil -> {
			perfil.getPermissoes().stream().filter(p -> {
				return p.getTipoItemMenu().equals(TipoItemMenu.SUBMENU) || p.getTipoItemMenu().equals(TipoItemMenu.ITEM);
			}).forEach(permissao -> {
				if (!permissoes.contains(permissao)) {
					permissoes.add(permissao);
				}
			});
		});

		// varre o programas recursivamente checando se o usuario tem ou nao direito
		List<MenuModel> menuModel = new ArrayList<>();
		programaRepository.findAll().stream().filter(p -> {
			return p.getProgramaPai() == null
					&& (p.getTipoItemMenu().equals(TipoItemMenu.SUBMENU) || p.getTipoItemMenu().equals(TipoItemMenu.ITEM) || p.getTipoItemMenu().equals(TipoItemMenu.DIVISOR));
		}).sorted(Comparator.comparing(Programa::getSeqMenu)).forEach(p -> {
			MenuModel menuItem = new MenuModel();
			menuItem.setId(p.getId());
			menuItem.setDisplayName(p.getDescricao());
			menuItem.setDisabled(true);
			menuItem.setDivider(p.getTipoItemMenu().equals(TipoItemMenu.DIVISOR));
			menuItem.setIconName(p.getIcone());
			menuItem.setRoute(p.getRoute());
			menuItem.setFather(null);
			if (p.getSubMenu() != null) {
				menuItem.setChildren(menuRecursivo(p.getSubMenu(), permissoes, menuItem));
			}
			menuModel.add(menuItem);

		});

		// sempre tem o sair
		MenuModel menuItem = new MenuModel();
		menuItem.setId("");
		menuItem.setDisplayName("");
		menuItem.setDisabled(false);
		menuItem.setDivider(true);
		menuItem.setIconName("");
		menuItem.setRoute("");
		menuItem.setFather(null);
		menuItem.setChildren(null);
		menuModel.add(menuItem);

		menuItem = new MenuModel();
		menuItem.setId("");
		menuItem.setDisplayName("Sair");
		menuItem.setDisabled(false);
		menuItem.setIconName("");
		menuItem.setRoute("logout");
		menuItem.setFather(null);
		menuItem.setChildren(null);
		menuModel.add(menuItem);

		return menuModel;
	}

	private List<MenuModel> menuRecursivo(List<Programa> lista, List<Programa> permissoes, MenuModel father) {

		List<MenuModel> menu = new ArrayList<>();
		lista.stream().filter(p -> {
			return (p.getTipoItemMenu().equals(TipoItemMenu.SUBMENU) || p.getTipoItemMenu().equals(TipoItemMenu.ITEM) || p.getTipoItemMenu().equals(TipoItemMenu.DIVISOR));
		}).forEach(permissao -> {
			if (permissao.getTipoItemMenu().equals(TipoItemMenu.DIVISOR)) {
				MenuModel menuItem = new MenuModel();
				menuItem.setId(permissao.getId());
				menuItem.setDivider(true);
				menuItem.setDisabled(false);
				menuItem.setDisplayName(null);
				menuItem.setIconName(null);
				menuItem.setRoute(null);
				menuItem.setFather(father);

				menuItem.setChildren(null);
				menu.add(menuItem);
			} else {
				MenuModel menuItem = new MenuModel();
				menuItem.setId(permissao.getId());
				menuItem.setDisabled(true);
				menuItem.setDivider(false);
				menuItem.setDisplayName(permissao.getDescricao());
				menuItem.setIconName(permissao.getIcone());
				menuItem.setRoute(permissao.getRoute());
				menuItem.setFather(father);
				if (permissao.getSubMenu() != null) {
					menuItem.setChildren(menuRecursivo(permissao.getSubMenu(), permissoes, menuItem));
				}
				if (!menu.contains(menuItem)) {
					menu.add(menuItem);
				}
				if (permissoes.contains(permissao)) {
					MenuModel i = menuItem;
					menu.get(menu.indexOf(i)).setDisabled(false);
					while (menu.indexOf(i) > -1 && menu.get(menu.indexOf(i)).getFather() != null) {
						menu.get(menu.indexOf(i)).getFather().setDisabled(false);
						i = menu.get(menu.indexOf(i)).getFather();
					}

				}
			}
		});
		return menu;
	}

	// @Override
	// public Optional<UsuarioDTO> findById(Long id) {
	// CriteriaBuilder cb = manager.getCriteriaBuilder();
	// CriteriaQuery<UsuarioDTO> criteriaQuery = cb.createQuery(UsuarioDTO.class);
	// Root<Usuario> rootFrom = criteriaQuery.from(Usuario.class);
	//
	// adicionaCampos(cb, criteriaQuery, rootFrom);
	//
	//
	// TypedQuery<UsuarioDTO> query = manager.createQuery(criteriaQuery);
	// return Optional.of(query.getSingleResult());
	// }
	//
	// private void adicionaCampos(CriteriaBuilder cb, CriteriaQuery<UsuarioDTO>
	// criteriaQuery, Root<Usuario> rootFrom) {
	//
	// criteriaQuery.select(cb.construct(UsuarioDTO.class,
	// rootFrom.get("id"),
	// rootFrom.get("email"),
	// rootFrom.get("login"),
	// rootFrom.get("fone"),
	// rootFrom.join("perfis").get("id"),
	// rootFrom.join("perfis").get("descricao")
	// ));
	// }

}
