package de.kvb.eps.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import de.kvb.eps.domain.Systeminstanz;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.SysteminstanzRepository;
import de.kvb.eps.repository.search.SysteminstanzSearchRepository;
import de.kvb.eps.service.dto.SysteminstanzCriteria;
import de.kvb.eps.service.dto.SysteminstanzDTO;
import de.kvb.eps.service.mapper.SysteminstanzMapper;

/**
 * Service for executing complex queries for {@link Systeminstanz} entities in the database.
 * The main input is a {@link SysteminstanzCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SysteminstanzDTO} or a {@link Page} of {@link SysteminstanzDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SysteminstanzQueryService extends QueryService<Systeminstanz> {

    private final Logger log = LoggerFactory.getLogger(SysteminstanzQueryService.class);

    private final SysteminstanzRepository systeminstanzRepository;

    private final SysteminstanzMapper systeminstanzMapper;

    private final SysteminstanzSearchRepository systeminstanzSearchRepository;

    public SysteminstanzQueryService(SysteminstanzRepository systeminstanzRepository, SysteminstanzMapper systeminstanzMapper, SysteminstanzSearchRepository systeminstanzSearchRepository) {
        this.systeminstanzRepository = systeminstanzRepository;
        this.systeminstanzMapper = systeminstanzMapper;
        this.systeminstanzSearchRepository = systeminstanzSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SysteminstanzDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SysteminstanzDTO> findByCriteria(SysteminstanzCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Systeminstanz> specification = createSpecification(criteria);
        return systeminstanzMapper.toDto(systeminstanzRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SysteminstanzDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SysteminstanzDTO> findByCriteria(SysteminstanzCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Systeminstanz> specification = createSpecification(criteria);
        return systeminstanzRepository.findAll(specification, page)
            .map(systeminstanzMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SysteminstanzCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Systeminstanz> specification = createSpecification(criteria);
        return systeminstanzRepository.count(specification);
    }

    /**
     * Function to convert {@link SysteminstanzCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Systeminstanz> createSpecification(SysteminstanzCriteria criteria) {
        Specification<Systeminstanz> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Systeminstanz_.id));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), Systeminstanz_.bezeichnung));
            }
            if (criteria.getGeraetNummer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGeraetNummer(), Systeminstanz_.geraetNummer));
            }
            if (criteria.getGeraetBaujahr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGeraetBaujahr(), Systeminstanz_.geraetBaujahr));
            }
            if (criteria.getGueltigBis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGueltigBis(), Systeminstanz_.gueltigBis));
            }
            if (criteria.getSystemnutzungId() != null) {
                specification = specification.and(buildSpecification(criteria.getSystemnutzungId(),
                    root -> root.join(Systeminstanz_.systemnutzungs, JoinType.LEFT).get(Systemnutzung_.id)));
            }
            if (criteria.getSystemtypId() != null) {
                specification = specification.and(buildSpecification(criteria.getSystemtypId(),
                    root -> root.join(Systeminstanz_.systemtyp, JoinType.LEFT).get(Systemtyp_.id)));
            }
            if (criteria.getBetriebsstaetteId() != null) {
                specification = specification.and(buildSpecification(criteria.getBetriebsstaetteId(),
                    root -> root.join(Systeminstanz_.betriebsstaette, JoinType.LEFT).get(Betriebsstaette_.id)));
            }
            if (criteria.getBetreiberId() != null) {
                specification = specification.and(buildSpecification(criteria.getBetreiberId(),
                    root -> root.join(Systeminstanz_.betreiber, JoinType.LEFT).get(Betreiber_.id)));
            }
        }
        return specification;
    }
}
