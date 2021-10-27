package br.com.acredita.authorizationserver.repositories;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.acredita.authorizationserver.DTOout.PerfilDTO;
import br.com.acredita.authorizationserver.enums.TipoItemMenu;
import br.com.acredita.authorizationserver.filters.PerfilFilter;
import br.com.acredita.authorizationserver.models.MenuModel;
import br.com.acredita.authorizationserver.models.Perfil;
import br.com.acredita.authorizationserver.models.Programa;


public class PerfilRepositoryImpl implements PerfilRepositoryQuery {
	@Autowired
	@Lazy
	private ProgramaRepository programaRepository;

	@Autowired
	@Lazy
	private PerfilRepository perfilRepository;

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<MenuModel> menuDoPerfil(Long perfilId) {
		Optional<Perfil> perfil = perfilRepository.findById(perfilId);

		List<MenuModel> menuModel = new ArrayList<>();
		List<Programa> permissoes = new ArrayList<>();
		if (perfil.isPresent()) {
			perfil.get().getPermissoes().stream().filter(p -> {
				return (!p.getTipoItemMenu().equals(TipoItemMenu.DIVISOR));

			}).forEach(permissao -> {
				if (!permissoes.contains(permissao)) {
					permissoes.add(permissao);
				}
			});
		}

		// varre o programas recursivamente checando se o usuario tem ou nao direito
		programaRepository.findAll().stream().filter(p -> {
			return p.getProgramaPai() == null && (!p.getTipoItemMenu().equals(TipoItemMenu.DIVISOR));
		}).sorted(Comparator.comparing(Programa::getSeqMenu)).forEach(p -> {
			MenuModel menuItem = new MenuModel();
			menuItem.setId(p.getId());
			menuItem.setDisplayName(p.getDescricao());
			menuItem.setDisabled(true);
			menuItem.setDivider(false);
			menuItem.setIconName(p.getIcone());
			menuItem.setRoute(p.getRoute());
			menuItem.setFather(null);
			if (p.getSubMenu() != null) {
				menuItem.setChildren(menuRecursivo(p.getSubMenu(), permissoes, menuItem));
			}
			menuModel.add(menuItem);

		});

		return menuModel;
	}

	private List<MenuModel> menuRecursivo(List<Programa> lista, List<Programa> permissoes, MenuModel father) {

		List<MenuModel> menu = new ArrayList<>();
		lista.stream().filter(p -> {
			return (!p.getTipoItemMenu().equals(TipoItemMenu.DIVISOR));
		}).forEach(permissao -> {
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

		});
		return menu;
	}

	
	@Override
	public List<PerfilDTO> findToCombobox(PerfilFilter filter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<PerfilDTO> criteriaQuery = cb.createQuery(PerfilDTO.class);
		Root<Perfil> rootFrom = criteriaQuery.from(Perfil.class);

		adicionaCampos(cb, criteriaQuery, rootFrom);
		
		Predicate[] predicates = createRestrictions(filter, cb, rootFrom);
		criteriaQuery.where(predicates);

		// ## ALTERADO MANUALMENTE A ORDERBY ##//
		criteriaQuery.orderBy(cb.asc(rootFrom.get("descricao")));

		TypedQuery<PerfilDTO> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@Override
	public List<Perfil> findToReport(PerfilFilter filter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Perfil> criteriaQuery = cb.createQuery(Perfil.class);
		Root<Perfil> rootFrom = criteriaQuery.from(Perfil.class);

		criteriaQuery.select(rootFrom); // select all 
		
		Predicate[] predicates = createRestrictions(filter, cb, rootFrom);
		criteriaQuery.where(predicates);

		// ## ALTERADO MANUALMENTE A ORDERBY ##//
		criteriaQuery.orderBy(cb.asc(rootFrom.get("descricao")));

		TypedQuery<Perfil> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

		
	
	@Override
	public Page<PerfilDTO> findByFilter(Pageable pageable, PerfilFilter filter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<PerfilDTO> criteriaQuery = cb.createQuery(PerfilDTO.class);
		Root<Perfil> rootFrom = criteriaQuery.from(Perfil.class);

		adicionaCampos(cb, criteriaQuery, rootFrom);

		Predicate[] predicates = createRestrictions((PerfilFilter) filter, cb, rootFrom);
		criteriaQuery.where(predicates);

		// ## ALTERADO MANUALMENTE A ORDERBY ##//
		criteriaQuery.orderBy(cb.asc(rootFrom.get("descricao")));

		TypedQuery<PerfilDTO> query = manager.createQuery(criteriaQuery);
		adicionarRestricoesDePaginacao(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, total((PerfilFilter) filter));
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<PerfilDTO> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	private Long total(PerfilFilter filter) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<Perfil> rootFrom = cq.from(Perfil.class);
		Predicate[] predicates = createRestrictions(filter, cb, rootFrom);
		cq.where(predicates);
		cq.select(cb.count(rootFrom));
		return manager.createQuery(cq).getSingleResult();
	}

	private void adicionaCampos(CriteriaBuilder cb, CriteriaQuery<PerfilDTO> criteriaQuery, Root<Perfil> rootFrom) {
		criteriaQuery.select(cb.construct(PerfilDTO.class, rootFrom.get("id"), rootFrom.get("descricao")));
	}

	private Predicate[] createRestrictions(PerfilFilter filter, CriteriaBuilder cb, Root<Perfil> rootFrom) {
		List<Predicate> predicates = new ArrayList<>();
		Predicate condition = null;

		if (filter.getId() != null && filter.getId() > 0) {
			condition = cb.equal(rootFrom.get("id"), filter.getId());
			predicates.add(condition);
		}

		if (!ObjectUtils.isEmpty(filter.getDescricao())) {
			condition = cb.like(rootFrom.get("descricao"), filter.getDescricao() + "%");
			predicates.add(condition);
		}

		return predicates.toArray(new Predicate[predicates.size()]);
	}


}
