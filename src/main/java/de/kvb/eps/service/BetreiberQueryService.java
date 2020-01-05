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

import de.kvb.eps.domain.Betreiber;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.BetreiberRepository;
import de.kvb.eps.repository.search.BetreiberSearchRepository;
import de.kvb.eps.service.dto.BetreiberCriteria;
import de.kvb.eps.service.dto.BetreiberDTO;
import de.kvb.eps.service.mapper.BetreiberMapper;

/**
 * Service for executing complex queries for {@link Betreiber} entities in the database.
 * The main input is a {@link BetreiberCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BetreiberDTO} or a {@link Page} of {@link BetreiberDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BetreiberQueryService extends QueryService<Betreiber> {

    private final Logger log = LoggerFactory.getLogger(BetreiberQueryService.class);

    private final BetreiberRepository betreiberRepository;

    private final BetreiberMapper betreiberMapper;

    private final BetreiberSearchRepository betreiberSearchRepository;

    public BetreiberQueryService(BetreiberRepository betreiberRepository, BetreiberMapper betreiberMapper, BetreiberSearchRepository betreiberSearchRepository) {
        this.betreiberRepository = betreiberRepository;
        this.betreiberMapper = betreiberMapper;
        this.betreiberSearchRepository = betreiberSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BetreiberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BetreiberDTO> findByCriteria(BetreiberCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Betreiber> specification = createSpecification(criteria);
        return betreiberMapper.toDto(betreiberRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BetreiberDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BetreiberDTO> findByCriteria(BetreiberCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Betreiber> specification = createSpecification(criteria);
        return betreiberRepository.findAll(specification, page)
            .map(betreiberMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BetreiberCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Betreiber> specification = createSpecification(criteria);
        return betreiberRepository.count(specification);
    }

    /**
     * Function to convert {@link BetreiberCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Betreiber> createSpecification(BetreiberCriteria criteria) {
        Specification<Betreiber> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Betreiber_.id));
            }
            if (criteria.getVorname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVorname(), Betreiber_.vorname));
            }
            if (criteria.getNachname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNachname(), Betreiber_.nachname));
            }
            if (criteria.getStrasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStrasse(), Betreiber_.strasse));
            }
            if (criteria.getHausnummer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHausnummer(), Betreiber_.hausnummer));
            }
            if (criteria.getPlz() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlz(), Betreiber_.plz));
            }
            if (criteria.getOrt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrt(), Betreiber_.ort));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), Betreiber_.bezeichnung));
            }
            if (criteria.getSysteminstanzId() != null) {
                specification = specification.and(buildSpecification(criteria.getSysteminstanzId(),
                    root -> root.join(Betreiber_.systeminstanzs, JoinType.LEFT).get(Systeminstanz_.id)));
            }
        }
        return specification;
    }
}
