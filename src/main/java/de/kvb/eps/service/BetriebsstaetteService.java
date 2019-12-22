package de.kvb.eps.service;

import de.kvb.eps.domain.Betriebsstaette;
import de.kvb.eps.repository.BetriebsstaetteRepository;
import de.kvb.eps.repository.search.BetriebsstaetteSearchRepository;
import de.kvb.eps.service.dto.BetriebsstaetteDTO;
import de.kvb.eps.service.mapper.BetriebsstaetteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Betriebsstaette}.
 */
@Service
@Transactional
public class BetriebsstaetteService {

    private final Logger log = LoggerFactory.getLogger(BetriebsstaetteService.class);

    private final BetriebsstaetteRepository betriebsstaetteRepository;

    private final BetriebsstaetteMapper betriebsstaetteMapper;

    private final BetriebsstaetteSearchRepository betriebsstaetteSearchRepository;

    public BetriebsstaetteService(BetriebsstaetteRepository betriebsstaetteRepository, BetriebsstaetteMapper betriebsstaetteMapper, BetriebsstaetteSearchRepository betriebsstaetteSearchRepository) {
        this.betriebsstaetteRepository = betriebsstaetteRepository;
        this.betriebsstaetteMapper = betriebsstaetteMapper;
        this.betriebsstaetteSearchRepository = betriebsstaetteSearchRepository;
    }

    /**
     * Save a betriebsstaette.
     *
     * @param betriebsstaetteDTO the entity to save.
     * @return the persisted entity.
     */
    public BetriebsstaetteDTO save(BetriebsstaetteDTO betriebsstaetteDTO) {
        log.debug("Request to save Betriebsstaette : {}", betriebsstaetteDTO);
        Betriebsstaette betriebsstaette = betriebsstaetteMapper.toEntity(betriebsstaetteDTO);
        betriebsstaette = betriebsstaetteRepository.save(betriebsstaette);
        BetriebsstaetteDTO result = betriebsstaetteMapper.toDto(betriebsstaette);
        betriebsstaetteSearchRepository.save(betriebsstaette);
        return result;
    }

    /**
     * Get all the betriebsstaettes.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BetriebsstaetteDTO> findAll() {
        log.debug("Request to get all Betriebsstaettes");
        return betriebsstaetteRepository.findAll().stream()
            .map(betriebsstaetteMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one betriebsstaette by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BetriebsstaetteDTO> findOne(Long id) {
        log.debug("Request to get Betriebsstaette : {}", id);
        return betriebsstaetteRepository.findById(id)
            .map(betriebsstaetteMapper::toDto);
    }

    /**
     * Delete the betriebsstaette by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Betriebsstaette : {}", id);
        betriebsstaetteRepository.deleteById(id);
        betriebsstaetteSearchRepository.deleteById(id);
    }

    /**
     * Search for the betriebsstaette corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BetriebsstaetteDTO> search(String query) {
        log.debug("Request to search Betriebsstaettes for query {}", query);
        return StreamSupport
            .stream(betriebsstaetteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(betriebsstaetteMapper::toDto)
            .collect(Collectors.toList());
    }
}
