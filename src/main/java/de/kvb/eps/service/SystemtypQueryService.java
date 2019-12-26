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

import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.SystemtypRepository;
import de.kvb.eps.repository.search.SystemtypSearchRepository;
import de.kvb.eps.service.dto.SystemtypCriteria;
import de.kvb.eps.service.dto.SystemtypDTO;
import de.kvb.eps.service.mapper.SystemtypMapper;

/**
 * Service for executing complex queries for {@link Systemtyp} entities in the database.
 * The main input is a {@link SystemtypCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemtypDTO} or a {@link Page} of {@link SystemtypDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemtypQueryService extends QueryService<Systemtyp> {

    private final Logger log = LoggerFactory.getLogger(SystemtypQueryService.class);

    private final SystemtypRepository systemtypRepository;

    private final SystemtypMapper systemtypMapper;

    private final SystemtypSearchRepository systemtypSearchRepository;

    public SystemtypQueryService(SystemtypRepository systemtypRepository, SystemtypMapper systemtypMapper, SystemtypSearchRepository systemtypSearchRepository) {
        this.systemtypRepository = systemtypRepository;
        this.systemtypMapper = systemtypMapper;
        this.systemtypSearchRepository = systemtypSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SystemtypDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemtypDTO> findByCriteria(SystemtypCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Systemtyp> specification = createSpecification(criteria);
        return systemtypMapper.toDto(systemtypRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemtypDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemtypDTO> findByCriteria(SystemtypCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Systemtyp> specification = createSpecification(criteria);
        return systemtypRepository.findAll(specification, page)
            .map(systemtypMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemtypCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Systemtyp> specification = createSpecification(criteria);
        return systemtypRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemtypCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Systemtyp> createSpecification(SystemtypCriteria criteria) {
        Specification<Systemtyp> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Systemtyp_.id));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), Systemtyp_.bezeichnung));
            }
            if (criteria.getGueltigBis() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGueltigBis(), Systemtyp_.gueltigBis));
            }
            if (criteria.getSysteminstanzId() != null) {
                specification = specification.and(buildSpecification(criteria.getSysteminstanzId(),
                    root -> root.join(Systemtyp_.systeminstanzs, JoinType.LEFT).get(Systeminstanz_.id)));
            }
            if (criteria.getGeraetId() != null) {
                specification = specification.and(buildSpecification(criteria.getGeraetId(),
                    root -> root.join(Systemtyp_.geraet, JoinType.LEFT).get(Geraet_.id)));
            }
            if (criteria.getZubehoerId() != null) {
                specification = specification.and(buildSpecification(criteria.getZubehoerId(),
                    root -> root.join(Systemtyp_.zubehoer, JoinType.LEFT).get(Zubehoer_.id)));
            }
        }
        return specification;
    }
}
