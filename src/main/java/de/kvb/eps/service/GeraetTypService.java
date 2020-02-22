package de.kvb.eps.service;

import de.kvb.eps.domain.GeraetTyp;
import de.kvb.eps.repository.GeraetTypRepository;
import de.kvb.eps.repository.search.GeraetTypSearchRepository;
import de.kvb.eps.service.dto.GeraetTypDTO;
import de.kvb.eps.service.mapper.GeraetTypMapper;
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
 * Service Implementation for managing {@link GeraetTyp}.
 */
@Service
@Transactional
public class GeraetTypService {

    private final Logger log = LoggerFactory.getLogger(GeraetTypService.class);

    private final GeraetTypRepository geraetTypRepository;

    private final GeraetTypMapper geraetTypMapper;

    private final GeraetTypSearchRepository geraetTypSearchRepository;

    public GeraetTypService(GeraetTypRepository geraetTypRepository, GeraetTypMapper geraetTypMapper, GeraetTypSearchRepository geraetTypSearchRepository) {
        this.geraetTypRepository = geraetTypRepository;
        this.geraetTypMapper = geraetTypMapper;
        this.geraetTypSearchRepository = geraetTypSearchRepository;
    }

    /**
     * Save a geraetTyp.
     *
     * @param geraetTypDTO the entity to save.
     * @return the persisted entity.
     */
    public GeraetTypDTO save(GeraetTypDTO geraetTypDTO) {
        log.debug("Request to save GeraetTyp : {}", geraetTypDTO);
        GeraetTyp geraetTyp = geraetTypMapper.toEntity(geraetTypDTO);
        geraetTyp = geraetTypRepository.save(geraetTyp);
        GeraetTypDTO result = geraetTypMapper.toDto(geraetTyp);
        geraetTypSearchRepository.save(geraetTyp);
        return result;
    }

    /**
     * Get all the geraetTyps.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GeraetTypDTO> findAll() {
        log.debug("Request to get all GeraetTyps");
        return geraetTypRepository.findAll().stream()
            .map(geraetTypMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one geraetTyp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GeraetTypDTO> findOne(Long id) {
        log.debug("Request to get GeraetTyp : {}", id);
        return geraetTypRepository.findById(id)
            .map(geraetTypMapper::toDto);
    }

    /**
     * Delete the geraetTyp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete GeraetTyp : {}", id);
        geraetTypRepository.deleteById(id);
        geraetTypSearchRepository.deleteById(id);
    }

    /**
     * Search for the geraetTyp corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GeraetTypDTO> search(String query) {
        log.debug("Request to search GeraetTyps for query {}", query);
        return StreamSupport
            .stream(geraetTypSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(geraetTypMapper::toDto)
            .collect(Collectors.toList());
    }
}
