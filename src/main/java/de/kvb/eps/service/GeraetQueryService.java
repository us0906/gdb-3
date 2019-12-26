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

import de.kvb.eps.domain.Geraet;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.GeraetRepository;
import de.kvb.eps.repository.search.GeraetSearchRepository;
import de.kvb.eps.service.dto.GeraetCriteria;
import de.kvb.eps.service.dto.GeraetDTO;
import de.kvb.eps.service.mapper.GeraetMapper;

/**
 * Service for executing complex queries for {@link Geraet} entities in the database.
 * The main input is a {@link GeraetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeraetDTO} or a {@link Page} of {@link GeraetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeraetQueryService extends QueryService<Geraet> {

    private final Logger log = LoggerFactory.getLogger(GeraetQueryService.class);

    private final GeraetRepository geraetRepository;

    private final GeraetMapper geraetMapper;

    private final GeraetSearchRepository geraetSearchRepository;

    public GeraetQueryService(GeraetRepository geraetRepository, GeraetMapper geraetMapper, GeraetSearchRepository geraetSearchRepository) {
        this.geraetRepository = geraetRepository;
        this.geraetMapper = geraetMapper;
        this.geraetSearchRepository = geraetSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GeraetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeraetDTO> findByCriteria(GeraetCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Geraet> specification = createSpecification(criteria);
        return geraetMapper.toDto(geraetRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeraetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeraetDTO> findByCriteria(GeraetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Geraet> specification = createSpecification(criteria);
        return geraetRepository.findAll(specification, page)
            .map(geraetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeraetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Geraet> specification = createSpecification(criteria);
        return geraetRepository.count(specification);
    }

    /**
     * Function to convert {@link GeraetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Geraet> createSpecification(GeraetCriteria criteria) {
        Specification<Geraet> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Geraet_.id));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), Geraet_.bezeichnung));
            }
            if (criteria.getGueltigBis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGueltigBis(), Geraet_.gueltigBis));
            }
            if (criteria.getSystemtypId() != null) {
                specification = specification.and(buildSpecification(criteria.getSystemtypId(),
                    root -> root.join(Geraet_.systemtyps, JoinType.LEFT).get(Systemtyp_.id)));
            }
            if (criteria.getGeraetTypId() != null) {
                specification = specification.and(buildSpecification(criteria.getGeraetTypId(),
                    root -> root.join(Geraet_.geraetTyp, JoinType.LEFT).get(GeraetTyp_.id)));
            }
            if (criteria.getHerstellerId() != null) {
                specification = specification.and(buildSpecification(criteria.getHerstellerId(),
                    root -> root.join(Geraet_.hersteller, JoinType.LEFT).get(Hersteller_.id)));
            }
        }
        return specification;
    }
}
