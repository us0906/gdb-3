package de.kvb.eps.service;

import de.kvb.eps.domain.Betreiber;
import de.kvb.eps.repository.BetreiberRepository;
import de.kvb.eps.repository.search.BetreiberSearchRepository;
import de.kvb.eps.service.dto.BetreiberDTO;
import de.kvb.eps.service.mapper.BetreiberMapper;
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
 * Service Implementation for managing {@link Betreiber}.
 */
@Service
@Transactional
public class BetreiberService {

    private final Logger log = LoggerFactory.getLogger(BetreiberService.class);

    private final BetreiberRepository betreiberRepository;

    private final BetreiberMapper betreiberMapper;

    private final BetreiberSearchRepository betreiberSearchRepository;

    public BetreiberService(BetreiberRepository betreiberRepository, BetreiberMapper betreiberMapper, BetreiberSearchRepository betreiberSearchRepository) {
        this.betreiberRepository = betreiberRepository;
        this.betreiberMapper = betreiberMapper;
        this.betreiberSearchRepository = betreiberSearchRepository;
    }

    /**
     * Save a betreiber.
     *
     * @param betreiberDTO the entity to save.
     * @return the persisted entity.
     */
    public BetreiberDTO save(BetreiberDTO betreiberDTO) {
        log.debug("Request to save Betreiber : {}", betreiberDTO);
        Betreiber betreiber = betreiberMapper.toEntity(betreiberDTO);
        betreiber = betreiberRepository.save(betreiber);
        BetreiberDTO result = betreiberMapper.toDto(betreiber);
        betreiberSearchRepository.save(betreiber);
        return result;
    }

    /**
     * Get all the betreibers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BetreiberDTO> findAll() {
        log.debug("Request to get all Betreibers");
        return betreiberRepository.findAll().stream()
            .map(betreiberMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one betreiber by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BetreiberDTO> findOne(Long id) {
        log.debug("Request to get Betreiber : {}", id);
        return betreiberRepository.findById(id)
            .map(betreiberMapper::toDto);
    }

    /**
     * Delete the betreiber by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Betreiber : {}", id);
        betreiberRepository.deleteById(id);
        betreiberSearchRepository.deleteById(id);
    }

    /**
     * Search for the betreiber corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BetreiberDTO> search(String query) {
        log.debug("Request to search Betreibers for query {}", query);
        return StreamSupport
            .stream(betreiberSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(betreiberMapper::toDto)
            .collect(Collectors.toList());
    }
}
