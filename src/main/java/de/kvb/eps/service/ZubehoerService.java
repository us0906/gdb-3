package de.kvb.eps.service;

import de.kvb.eps.domain.Zubehoer;
import de.kvb.eps.repository.ZubehoerRepository;
import de.kvb.eps.repository.search.ZubehoerSearchRepository;
import de.kvb.eps.service.dto.ZubehoerDTO;
import de.kvb.eps.service.mapper.ZubehoerMapper;
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
 * Service Implementation for managing {@link Zubehoer}.
 */
@Service
@Transactional
public class ZubehoerService {

    private final Logger log = LoggerFactory.getLogger(ZubehoerService.class);

    private final ZubehoerRepository zubehoerRepository;

    private final ZubehoerMapper zubehoerMapper;

    private final ZubehoerSearchRepository zubehoerSearchRepository;

    public ZubehoerService(ZubehoerRepository zubehoerRepository, ZubehoerMapper zubehoerMapper, ZubehoerSearchRepository zubehoerSearchRepository) {
        this.zubehoerRepository = zubehoerRepository;
        this.zubehoerMapper = zubehoerMapper;
        this.zubehoerSearchRepository = zubehoerSearchRepository;
    }

    /**
     * Save a zubehoer.
     *
     * @param zubehoerDTO the entity to save.
     * @return the persisted entity.
     */
    public ZubehoerDTO save(ZubehoerDTO zubehoerDTO) {
        log.debug("Request to save Zubehoer : {}", zubehoerDTO);
        Zubehoer zubehoer = zubehoerMapper.toEntity(zubehoerDTO);
        zubehoer = zubehoerRepository.save(zubehoer);
        ZubehoerDTO result = zubehoerMapper.toDto(zubehoer);
        zubehoerSearchRepository.save(zubehoer);
        return result;
    }

    /**
     * Get all the zubehoers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ZubehoerDTO> findAll() {
        log.debug("Request to get all Zubehoers");
        return zubehoerRepository.findAll().stream()
            .map(zubehoerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one zubehoer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ZubehoerDTO> findOne(Long id) {
        log.debug("Request to get Zubehoer : {}", id);
        return zubehoerRepository.findById(id)
            .map(zubehoerMapper::toDto);
    }

    /**
     * Delete the zubehoer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Zubehoer : {}", id);
        zubehoerRepository.deleteById(id);
        zubehoerSearchRepository.deleteById(id);
    }

    /**
     * Search for the zubehoer corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ZubehoerDTO> search(String query) {
        log.debug("Request to search Zubehoers for query {}", query);
        return StreamSupport
            .stream(zubehoerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(zubehoerMapper::toDto)
            .collect(Collectors.toList());
    }
}
