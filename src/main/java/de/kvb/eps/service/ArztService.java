package de.kvb.eps.service;

import de.kvb.eps.domain.Arzt;
import de.kvb.eps.repository.ArztRepository;
import de.kvb.eps.repository.search.ArztSearchRepository;
import de.kvb.eps.service.dto.ArztDTO;
import de.kvb.eps.service.mapper.ArztMapper;
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
 * Service Implementation for managing {@link Arzt}.
 */
@Service
@Transactional
public class ArztService {

    private final Logger log = LoggerFactory.getLogger(ArztService.class);

    private final ArztRepository arztRepository;

    private final ArztMapper arztMapper;

    private final ArztSearchRepository arztSearchRepository;

    public ArztService(ArztRepository arztRepository, ArztMapper arztMapper, ArztSearchRepository arztSearchRepository) {
        this.arztRepository = arztRepository;
        this.arztMapper = arztMapper;
        this.arztSearchRepository = arztSearchRepository;
    }

    /**
     * Save a arzt.
     *
     * @param arztDTO the entity to save.
     * @return the persisted entity.
     */
    public ArztDTO save(ArztDTO arztDTO) {
        log.debug("Request to save Arzt : {}", arztDTO);
        Arzt arzt = arztMapper.toEntity(arztDTO);
        arzt = arztRepository.save(arzt);
        ArztDTO result = arztMapper.toDto(arzt);
        arztSearchRepository.save(arzt);
        return result;
    }

    /**
     * Get all the arzts.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ArztDTO> findAll() {
        log.debug("Request to get all Arzts");
        return arztRepository.findAll().stream()
            .map(arztMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one arzt by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ArztDTO> findOne(Long id) {
        log.debug("Request to get Arzt : {}", id);
        return arztRepository.findById(id)
            .map(arztMapper::toDto);
    }

    /**
     * Delete the arzt by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Arzt : {}", id);
        arztRepository.deleteById(id);
        arztSearchRepository.deleteById(id);
    }

    /**
     * Search for the arzt corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ArztDTO> search(String query) {
        log.debug("Request to search Arzts for query {}", query);
        return StreamSupport
            .stream(arztSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(arztMapper::toDto)
            .collect(Collectors.toList());
    }
}
