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

import de.kvb.eps.domain.Hersteller;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.HerstellerRepository;
import de.kvb.eps.repository.search.HerstellerSearchRepository;
import de.kvb.eps.service.dto.HerstellerCriteria;
import de.kvb.eps.service.dto.HerstellerDTO;
import de.kvb.eps.service.mapper.HerstellerMapper;

/**
 * Service for executing complex queries for {@link Hersteller} entities in the database.
 * The main input is a {@link HerstellerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HerstellerDTO} or a {@link Page} of {@link HerstellerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HerstellerQueryService extends QueryService<Hersteller> {

    private final Logger log = LoggerFactory.getLogger(HerstellerQueryService.class);

    private final HerstellerRepository herstellerRepository;

    private final HerstellerMapper herstellerMapper;

    private final HerstellerSearchRepository herstellerSearchRepository;

    public HerstellerQueryService(HerstellerRepository herstellerRepository, HerstellerMapper herstellerMapper, HerstellerSearchRepository herstellerSearchRepository) {
        this.herstellerRepository = herstellerRepository;
        this.herstellerMapper = herstellerMapper;
        this.herstellerSearchRepository = herstellerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HerstellerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HerstellerDTO> findByCriteria(HerstellerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Hersteller> specification = createSpecification(criteria);
        return herstellerMapper.toDto(herstellerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HerstellerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HerstellerDTO> findByCriteria(HerstellerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Hersteller> specification = createSpecification(criteria);
        return herstellerRepository.findAll(specification, page)
            .map(herstellerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HerstellerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Hersteller> specification = createSpecification(criteria);
        return herstellerRepository.count(specification);
    }

    /**
     * Function to convert {@link HerstellerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Hersteller> createSpecification(HerstellerCriteria criteria) {
        Specification<Hersteller> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Hersteller_.id));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), Hersteller_.bezeichnung));
            }
            if (criteria.getGueltigBis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGueltigBis(), Hersteller_.gueltigBis));
            }
            if (criteria.getGeraetId() != null) {
                specification = specification.and(buildSpecification(criteria.getGeraetId(),
                    root -> root.join(Hersteller_.geraets, JoinType.LEFT).get(Geraet_.id)));
            }
            if (criteria.getZubehoerId() != null) {
                specification = specification.and(buildSpecification(criteria.getZubehoerId(),
                    root -> root.join(Hersteller_.zubehoers, JoinType.LEFT).get(Zubehoer_.id)));
            }
        }
        return specification;
    }
}
