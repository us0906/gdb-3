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

import de.kvb.eps.domain.Arzt;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.ArztRepository;
import de.kvb.eps.repository.search.ArztSearchRepository;
import de.kvb.eps.service.dto.ArztCriteria;
import de.kvb.eps.service.dto.ArztDTO;
import de.kvb.eps.service.mapper.ArztMapper;

/**
 * Service for executing complex queries for {@link Arzt} entities in the database.
 * The main input is a {@link ArztCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ArztDTO} or a {@link Page} of {@link ArztDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArztQueryService extends QueryService<Arzt> {

    private final Logger log = LoggerFactory.getLogger(ArztQueryService.class);

    private final ArztRepository arztRepository;

    private final ArztMapper arztMapper;

    private final ArztSearchRepository arztSearchRepository;

    public ArztQueryService(ArztRepository arztRepository, ArztMapper arztMapper, ArztSearchRepository arztSearchRepository) {
        this.arztRepository = arztRepository;
        this.arztMapper = arztMapper;
        this.arztSearchRepository = arztSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ArztDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ArztDTO> findByCriteria(ArztCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Arzt> specification = createSpecification(criteria);
        return arztMapper.toDto(arztRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ArztDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArztDTO> findByCriteria(ArztCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Arzt> specification = createSpecification(criteria);
        return arztRepository.findAll(specification, page)
            .map(arztMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArztCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Arzt> specification = createSpecification(criteria);
        return arztRepository.count(specification);
    }

    /**
     * Function to convert {@link ArztCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Arzt> createSpecification(ArztCriteria criteria) {
        Specification<Arzt> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Arzt_.id));
            }
            if (criteria.getLanr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanr(), Arzt_.lanr));
            }
            if (criteria.getTitel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitel(), Arzt_.titel));
            }
            if (criteria.getVorname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVorname(), Arzt_.vorname));
            }
            if (criteria.getNachname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNachname(), Arzt_.nachname));
            }
            if (criteria.getSystemnutzungId() != null) {
                specification = specification.and(buildSpecification(criteria.getSystemnutzungId(),
                    root -> root.join(Arzt_.systemnutzungs, JoinType.LEFT).get(Systemnutzung_.id)));
            }
        }
        return specification;
    }
}
