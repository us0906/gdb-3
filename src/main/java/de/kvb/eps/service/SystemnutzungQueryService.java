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

import de.kvb.eps.domain.Systemnutzung;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.SystemnutzungRepository;
import de.kvb.eps.repository.search.SystemnutzungSearchRepository;
import de.kvb.eps.service.dto.SystemnutzungCriteria;
import de.kvb.eps.service.dto.SystemnutzungDTO;
import de.kvb.eps.service.mapper.SystemnutzungMapper;

/**
 * Service for executing complex queries for {@link Systemnutzung} entities in the database.
 * The main input is a {@link SystemnutzungCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SystemnutzungDTO} or a {@link Page} of {@link SystemnutzungDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemnutzungQueryService extends QueryService<Systemnutzung> {

    private final Logger log = LoggerFactory.getLogger(SystemnutzungQueryService.class);

    private final SystemnutzungRepository systemnutzungRepository;

    private final SystemnutzungMapper systemnutzungMapper;

    private final SystemnutzungSearchRepository systemnutzungSearchRepository;

    public SystemnutzungQueryService(SystemnutzungRepository systemnutzungRepository, SystemnutzungMapper systemnutzungMapper, SystemnutzungSearchRepository systemnutzungSearchRepository) {
        this.systemnutzungRepository = systemnutzungRepository;
        this.systemnutzungMapper = systemnutzungMapper;
        this.systemnutzungSearchRepository = systemnutzungSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SystemnutzungDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SystemnutzungDTO> findByCriteria(SystemnutzungCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Systemnutzung> specification = createSpecification(criteria);
        return systemnutzungMapper.toDto(systemnutzungRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SystemnutzungDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemnutzungDTO> findByCriteria(SystemnutzungCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Systemnutzung> specification = createSpecification(criteria);
        return systemnutzungRepository.findAll(specification, page)
            .map(systemnutzungMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemnutzungCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Systemnutzung> specification = createSpecification(criteria);
        return systemnutzungRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemnutzungCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Systemnutzung> createSpecification(SystemnutzungCriteria criteria) {
        Specification<Systemnutzung> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Systemnutzung_.id));
            }
            if (criteria.getSysteminstanzId() != null) {
                specification = specification.and(buildSpecification(criteria.getSysteminstanzId(),
                    root -> root.join(Systemnutzung_.systeminstanz, JoinType.LEFT).get(Systeminstanz_.id)));
            }
            if (criteria.getArztId() != null) {
                specification = specification.and(buildSpecification(criteria.getArztId(),
                    root -> root.join(Systemnutzung_.arzt, JoinType.LEFT).get(Arzt_.id)));
            }
        }
        return specification;
    }
}
