package br.com.acredita.applicationlog.repositories;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import br.com.acredita.applicationlog.DTOout.LogDTOout;
import br.com.acredita.applicationlog.enums.SimNao;
import br.com.acredita.applicationlog.filters.LogFilter;
import br.com.acredita.applicationlog.models.Log;
import br.com.acredita.applicationlog.models.Log_;

public class LogRepositoryImpl implements LogRepositoryQuery {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<LogDTOout> findByFilter(Pageable pageable, LogFilter filter) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<LogDTOout> criteriaQuery = cb.createQuery(LogDTOout.class);
		Root<Log> rootFrom = criteriaQuery.from(Log.class);
        adicionaCampos(cb, criteriaQuery, rootFrom);
        Predicate[] predicates = createRestrictions(filter, cb, rootFrom);
        criteriaQuery.where(predicates);

        // ## ALTERADO MANUALMENTE A ORDERBY ##//
        criteriaQuery.orderBy(cb.asc(rootFrom.get(Log_.DATE_AND_TIME)));        

        TypedQuery<LogDTOout> query = manager.createQuery(criteriaQuery);
        adicionarRestricoesDePaginacao(query, pageable);
        return new PageImpl<>(query.getResultList(), pageable, total(filter));
    }

    @Override
    public List<Log> findToReport(LogFilter filter) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Log> criteriaQuery = cb.createQuery(Log.class);
		Root<Log> rootFrom = criteriaQuery.from(Log.class);
		criteriaQuery.select(rootFrom);
		Predicate[] predicates = createRestrictions(filter, cb, rootFrom);
		criteriaQuery.where(predicates);

		// ## ALTERADO MANUALMENTE A ORDERBY ##//
		criteriaQuery.orderBy(cb.asc(rootFrom.get(Log_.DATE_AND_TIME)));

		TypedQuery<Log> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
    }

    @Override
    public List<LogDTOout> findToCombobox() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<LogDTOout> criteriaQuery = builder.createQuery(LogDTOout.class);
		Root<Log> rootFrom = criteriaQuery.from(Log.class);
		adicionaCampos(builder, criteriaQuery, rootFrom);
        
         criteriaQuery.where(builder.equal(rootFrom.get(Log_.ACTIVE), SimNao.SIM)); //somente os ativos 

		TypedQuery<LogDTOout> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
    }

    private void adicionaCampos(CriteriaBuilder cb, CriteriaQuery<LogDTOout> criteriaQuery, Root<Log> rootFrom) {
        criteriaQuery
                .select(cb.construct(LogDTOout.class, 
                rootFrom.get(Log_.ID), 
                rootFrom.get(Log_.DATE_AND_TIME),
                rootFrom.get(Log_.MESSAGE)
                ));
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
        query.setFirstResult(primeiroRegistroDaPagina);
        query.setMaxResults(totalRegistrosPorPagina);
    }

    private Long total(LogFilter filter) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Log> rootFrom = cq.from(Log.class);
        Predicate[] predicates = createRestrictions(filter, cb, rootFrom);
        cq.where(predicates);
        cq.select(cb.count(rootFrom));
        return manager.createQuery(cq).getSingleResult();
    }

    private Predicate[] createRestrictions(LogFilter filter, CriteriaBuilder cb, Root<Log> rootFrom) {
        List<Predicate> predicates = new ArrayList<>();
        Predicate condition = null;
        if (!ObjectUtils.isEmpty(filter.getId())) {
            condition = cb.equal(rootFrom.get(Log_.ID), filter.getId());
            predicates.add(condition);
        }

        if (!ObjectUtils.isEmpty(filter.getMessage())) {
            condition = cb.like(rootFrom.get(Log_.MESSAGE), "%" + filter.getMessage() + "%");
            predicates.add(condition);
        }

        if (!ObjectUtils.isEmpty(filter.getStartDateTime())) {
            condition = cb.greaterThanOrEqualTo(rootFrom.get(Log_.DATE_AND_TIME), filter.getStartDateTime());
            predicates.add(condition);
        }

        if (!ObjectUtils.isEmpty(filter.getEndDateTime())) {
            condition = cb.lessThanOrEqualTo(rootFrom.get(Log_.DATE_AND_TIME), filter.getEndDateTime());
            predicates.add(condition);
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

}
