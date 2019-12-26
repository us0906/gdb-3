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

import de.kvb.eps.domain.Zubehoer;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.ZubehoerRepository;
import de.kvb.eps.repository.search.ZubehoerSearchRepository;
import de.kvb.eps.service.dto.ZubehoerCriteria;
import de.kvb.eps.service.dto.ZubehoerDTO;
import de.kvb.eps.service.mapper.ZubehoerMapper;

/**
 * Service for executing complex queries for {@link Zubehoer} entities in the database.
 * The main input is a {@link ZubehoerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ZubehoerDTO} or a {@link Page} of {@link ZubehoerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ZubehoerQueryService extends QueryService<Zubehoer> {

    private final Logger log = LoggerFactory.getLogger(ZubehoerQueryService.class);

    private final ZubehoerRepository zubehoerRepository;

    private final ZubehoerMapper zubehoerMapper;

    private final ZubehoerSearchRepository zubehoerSearchRepository;

    public ZubehoerQueryService(ZubehoerRepository zubehoerRepository, ZubehoerMapper zubehoerMapper, ZubehoerSearchRepository zubehoerSearchRepository) {
        this.zubehoerRepository = zubehoerRepository;
        this.zubehoerMapper = zubehoerMapper;
        this.zubehoerSearchRepository = zubehoerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ZubehoerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ZubehoerDTO> findByCriteria(ZubehoerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Zubehoer> specification = createSpecification(criteria);
        return zubehoerMapper.toDto(zubehoerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ZubehoerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ZubehoerDTO> findByCriteria(ZubehoerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Zubehoer> specification = createSpecification(criteria);
        return zubehoerRepository.findAll(specification, page)
            .map(zubehoerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ZubehoerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Zubehoer> specification = createSpecification(criteria);
        return zubehoerRepository.count(specification);
    }

    /**
     * Function to convert {@link ZubehoerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Zubehoer> createSpecification(ZubehoerCriteria criteria) {
        Specification<Zubehoer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Zubehoer_.id));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), Zubehoer_.bezeichnung));
            }
            if (criteria.getGueltigBis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGueltigBis(), Zubehoer_.gueltigBis));
            }
            if (criteria.getSystemtypId() != null) {
                specification = specification.and(buildSpecification(criteria.getSystemtypId(),
                    root -> root.join(Zubehoer_.systemtyps, JoinType.LEFT).get(Systemtyp_.id)));
            }
            if (criteria.getHerstellerId() != null) {
                specification = specification.and(buildSpecification(criteria.getHerstellerId(),
                    root -> root.join(Zubehoer_.hersteller, JoinType.LEFT).get(Hersteller_.id)));
            }
            if (criteria.getZubehoerTypId() != null) {
                specification = specification.and(buildSpecification(criteria.getZubehoerTypId(),
                    root -> root.join(Zubehoer_.zubehoerTyp, JoinType.LEFT).get(ZubehoerTyp_.id)));
            }
        }
        return specification;
    }
}
