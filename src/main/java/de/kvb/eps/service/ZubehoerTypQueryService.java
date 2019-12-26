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

import de.kvb.eps.domain.ZubehoerTyp;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.ZubehoerTypRepository;
import de.kvb.eps.repository.search.ZubehoerTypSearchRepository;
import de.kvb.eps.service.dto.ZubehoerTypCriteria;
import de.kvb.eps.service.dto.ZubehoerTypDTO;
import de.kvb.eps.service.mapper.ZubehoerTypMapper;

/**
 * Service for executing complex queries for {@link ZubehoerTyp} entities in the database.
 * The main input is a {@link ZubehoerTypCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ZubehoerTypDTO} or a {@link Page} of {@link ZubehoerTypDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ZubehoerTypQueryService extends QueryService<ZubehoerTyp> {

    private final Logger log = LoggerFactory.getLogger(ZubehoerTypQueryService.class);

    private final ZubehoerTypRepository zubehoerTypRepository;

    private final ZubehoerTypMapper zubehoerTypMapper;

    private final ZubehoerTypSearchRepository zubehoerTypSearchRepository;

    public ZubehoerTypQueryService(ZubehoerTypRepository zubehoerTypRepository, ZubehoerTypMapper zubehoerTypMapper, ZubehoerTypSearchRepository zubehoerTypSearchRepository) {
        this.zubehoerTypRepository = zubehoerTypRepository;
        this.zubehoerTypMapper = zubehoerTypMapper;
        this.zubehoerTypSearchRepository = zubehoerTypSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ZubehoerTypDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ZubehoerTypDTO> findByCriteria(ZubehoerTypCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ZubehoerTyp> specification = createSpecification(criteria);
        return zubehoerTypMapper.toDto(zubehoerTypRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ZubehoerTypDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ZubehoerTypDTO> findByCriteria(ZubehoerTypCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ZubehoerTyp> specification = createSpecification(criteria);
        return zubehoerTypRepository.findAll(specification, page)
            .map(zubehoerTypMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ZubehoerTypCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ZubehoerTyp> specification = createSpecification(criteria);
        return zubehoerTypRepository.count(specification);
    }

    /**
     * Function to convert {@link ZubehoerTypCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ZubehoerTyp> createSpecification(ZubehoerTypCriteria criteria) {
        Specification<ZubehoerTyp> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ZubehoerTyp_.id));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), ZubehoerTyp_.bezeichnung));
            }
            if (criteria.getGueltigBis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGueltigBis(), ZubehoerTyp_.gueltigBis));
            }
            if (criteria.getTechnologie() != null) {
                specification = specification.and(buildSpecification(criteria.getTechnologie(), ZubehoerTyp_.technologie));
            }
            if (criteria.getZubehoerId() != null) {
                specification = specification.and(buildSpecification(criteria.getZubehoerId(),
                    root -> root.join(ZubehoerTyp_.zubehoers, JoinType.LEFT).get(Zubehoer_.id)));
            }
        }
        return specification;
    }
}
