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

import de.kvb.eps.domain.GeraetTyp;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.GeraetTypRepository;
import de.kvb.eps.repository.search.GeraetTypSearchRepository;
import de.kvb.eps.service.dto.GeraetTypCriteria;
import de.kvb.eps.service.dto.GeraetTypDTO;
import de.kvb.eps.service.mapper.GeraetTypMapper;

/**
 * Service for executing complex queries for {@link GeraetTyp} entities in the database.
 * The main input is a {@link GeraetTypCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GeraetTypDTO} or a {@link Page} of {@link GeraetTypDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GeraetTypQueryService extends QueryService<GeraetTyp> {

    private final Logger log = LoggerFactory.getLogger(GeraetTypQueryService.class);

    private final GeraetTypRepository geraetTypRepository;

    private final GeraetTypMapper geraetTypMapper;

    private final GeraetTypSearchRepository geraetTypSearchRepository;

    public GeraetTypQueryService(GeraetTypRepository geraetTypRepository, GeraetTypMapper geraetTypMapper, GeraetTypSearchRepository geraetTypSearchRepository) {
        this.geraetTypRepository = geraetTypRepository;
        this.geraetTypMapper = geraetTypMapper;
        this.geraetTypSearchRepository = geraetTypSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GeraetTypDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GeraetTypDTO> findByCriteria(GeraetTypCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GeraetTyp> specification = createSpecification(criteria);
        return geraetTypMapper.toDto(geraetTypRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GeraetTypDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GeraetTypDTO> findByCriteria(GeraetTypCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GeraetTyp> specification = createSpecification(criteria);
        return geraetTypRepository.findAll(specification, page)
            .map(geraetTypMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GeraetTypCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GeraetTyp> specification = createSpecification(criteria);
        return geraetTypRepository.count(specification);
    }

    /**
     * Function to convert {@link GeraetTypCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GeraetTyp> createSpecification(GeraetTypCriteria criteria) {
        Specification<GeraetTyp> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GeraetTyp_.id));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), GeraetTyp_.bezeichnung));
            }
            if (criteria.getGueltigBis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGueltigBis(), GeraetTyp_.gueltigBis));
            }
            if (criteria.getTechnologie() != null) {
                specification = specification.and(buildSpecification(criteria.getTechnologie(), GeraetTyp_.technologie));
            }
            if (criteria.getGeraetId() != null) {
                specification = specification.and(buildSpecification(criteria.getGeraetId(),
                    root -> root.join(GeraetTyp_.geraets, JoinType.LEFT).get(Geraet_.id)));
            }
        }
        return specification;
    }
}
