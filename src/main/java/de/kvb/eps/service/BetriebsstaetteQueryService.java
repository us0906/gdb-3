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

import de.kvb.eps.domain.Betriebsstaette;
import de.kvb.eps.domain.*; // for static metamodels
import de.kvb.eps.repository.BetriebsstaetteRepository;
import de.kvb.eps.repository.search.BetriebsstaetteSearchRepository;
import de.kvb.eps.service.dto.BetriebsstaetteCriteria;
import de.kvb.eps.service.dto.BetriebsstaetteDTO;
import de.kvb.eps.service.mapper.BetriebsstaetteMapper;

/**
 * Service for executing complex queries for {@link Betriebsstaette} entities in the database.
 * The main input is a {@link BetriebsstaetteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BetriebsstaetteDTO} or a {@link Page} of {@link BetriebsstaetteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BetriebsstaetteQueryService extends QueryService<Betriebsstaette> {

    private final Logger log = LoggerFactory.getLogger(BetriebsstaetteQueryService.class);

    private final BetriebsstaetteRepository betriebsstaetteRepository;

    private final BetriebsstaetteMapper betriebsstaetteMapper;

    private final BetriebsstaetteSearchRepository betriebsstaetteSearchRepository;

    public BetriebsstaetteQueryService(BetriebsstaetteRepository betriebsstaetteRepository, BetriebsstaetteMapper betriebsstaetteMapper, BetriebsstaetteSearchRepository betriebsstaetteSearchRepository) {
        this.betriebsstaetteRepository = betriebsstaetteRepository;
        this.betriebsstaetteMapper = betriebsstaetteMapper;
        this.betriebsstaetteSearchRepository = betriebsstaetteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BetriebsstaetteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BetriebsstaetteDTO> findByCriteria(BetriebsstaetteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Betriebsstaette> specification = createSpecification(criteria);
        return betriebsstaetteMapper.toDto(betriebsstaetteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BetriebsstaetteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BetriebsstaetteDTO> findByCriteria(BetriebsstaetteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Betriebsstaette> specification = createSpecification(criteria);
        return betriebsstaetteRepository.findAll(specification, page)
            .map(betriebsstaetteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BetriebsstaetteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Betriebsstaette> specification = createSpecification(criteria);
        return betriebsstaetteRepository.count(specification);
    }

    /**
     * Function to convert {@link BetriebsstaetteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Betriebsstaette> createSpecification(BetriebsstaetteCriteria criteria) {
        Specification<Betriebsstaette> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Betriebsstaette_.id));
            }
            if (criteria.getBsnr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBsnr(), Betriebsstaette_.bsnr));
            }
            if (criteria.getStrasse() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStrasse(), Betriebsstaette_.strasse));
            }
            if (criteria.getHausnummer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHausnummer(), Betriebsstaette_.hausnummer));
            }
            if (criteria.getPlz() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlz(), Betriebsstaette_.plz));
            }
            if (criteria.getOrt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrt(), Betriebsstaette_.ort));
            }
            if (criteria.getBezeichnung() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBezeichnung(), Betriebsstaette_.bezeichnung));
            }
            if (criteria.getSysteminstanzId() != null) {
                specification = specification.and(buildSpecification(criteria.getSysteminstanzId(),
                    root -> root.join(Betriebsstaette_.systeminstanzs, JoinType.LEFT).get(Systeminstanz_.id)));
            }
        }
        return specification;
    }
}
