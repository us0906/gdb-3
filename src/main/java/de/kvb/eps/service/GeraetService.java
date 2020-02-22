package de.kvb.eps.service;

import de.kvb.eps.domain.Geraet;
import de.kvb.eps.repository.GeraetRepository;
import de.kvb.eps.repository.search.GeraetSearchRepository;
import de.kvb.eps.service.dto.GeraetDTO;
import de.kvb.eps.service.mapper.GeraetMapper;
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
 * Service Implementation for managing {@link Geraet}.
 */
@Service
@Transactional
public class GeraetService {

    private final Logger log = LoggerFactory.getLogger(GeraetService.class);

    private final GeraetRepository geraetRepository;

    private final GeraetMapper geraetMapper;

    private final GeraetSearchRepository geraetSearchRepository;

    public GeraetService(GeraetRepository geraetRepository, GeraetMapper geraetMapper, GeraetSearchRepository geraetSearchRepository) {
        this.geraetRepository = geraetRepository;
        this.geraetMapper = geraetMapper;
        this.geraetSearchRepository = geraetSearchRepository;
    }

    /**
     * Save a geraet.
     *
     * @param geraetDTO the entity to save.
     * @return the persisted entity.
     */
    public GeraetDTO save(GeraetDTO geraetDTO) {
        log.debug("Request to save Geraet : {}", geraetDTO);
        Geraet geraet = geraetMapper.toEntity(geraetDTO);
        geraet = geraetRepository.save(geraet);
        GeraetDTO result = geraetMapper.toDto(geraet);
        geraetSearchRepository.save(geraet);
        return result;
    }

    /**
     * Get all the geraets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GeraetDTO> findAll() {
        log.debug("Request to get all Geraets");
        return geraetRepository.findAll().stream()
            .map(geraetMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one geraet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GeraetDTO> findOne(Long id) {
        log.debug("Request to get Geraet : {}", id);
        return geraetRepository.findById(id)
            .map(geraetMapper::toDto);
    }

    /**
     * Delete the geraet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Geraet : {}", id);
        geraetRepository.deleteById(id);
        geraetSearchRepository.deleteById(id);
    }

    /**
     * Search for the geraet corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GeraetDTO> search(String query) {
        log.debug("Request to search Geraets for query {}", query);
        return StreamSupport
            .stream(geraetSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(geraetMapper::toDto)
            .collect(Collectors.toList());
    }
}
