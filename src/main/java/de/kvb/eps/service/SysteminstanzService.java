package de.kvb.eps.service;

import de.kvb.eps.domain.Systeminstanz;
import de.kvb.eps.repository.SysteminstanzRepository;
import de.kvb.eps.repository.search.SysteminstanzSearchRepository;
import de.kvb.eps.service.dto.SysteminstanzDTO;
import de.kvb.eps.service.mapper.SysteminstanzMapper;
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
 * Service Implementation for managing {@link Systeminstanz}.
 */
@Service
@Transactional
public class SysteminstanzService {

    private final Logger log = LoggerFactory.getLogger(SysteminstanzService.class);

    private final SysteminstanzRepository systeminstanzRepository;

    private final SysteminstanzMapper systeminstanzMapper;

    private final SysteminstanzSearchRepository systeminstanzSearchRepository;

    public SysteminstanzService(SysteminstanzRepository systeminstanzRepository, SysteminstanzMapper systeminstanzMapper, SysteminstanzSearchRepository systeminstanzSearchRepository) {
        this.systeminstanzRepository = systeminstanzRepository;
        this.systeminstanzMapper = systeminstanzMapper;
        this.systeminstanzSearchRepository = systeminstanzSearchRepository;
    }

    /**
     * Save a systeminstanz.
     *
     * @param systeminstanzDTO the entity to save.
     * @return the persisted entity.
     */
    public SysteminstanzDTO save(SysteminstanzDTO systeminstanzDTO) {
        log.debug("Request to save Systeminstanz : {}", systeminstanzDTO);
        Systeminstanz systeminstanz = systeminstanzMapper.toEntity(systeminstanzDTO);
        systeminstanz = systeminstanzRepository.save(systeminstanz);
        SysteminstanzDTO result = systeminstanzMapper.toDto(systeminstanz);
        systeminstanzSearchRepository.save(systeminstanz);
        return result;
    }

    /**
     * Get all the systeminstanzs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SysteminstanzDTO> findAll() {
        log.debug("Request to get all Systeminstanzs");
        return systeminstanzRepository.findAll().stream()
            .map(systeminstanzMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one systeminstanz by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SysteminstanzDTO> findOne(Long id) {
        log.debug("Request to get Systeminstanz : {}", id);
        return systeminstanzRepository.findById(id)
            .map(systeminstanzMapper::toDto);
    }

    /**
     * Delete the systeminstanz by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Systeminstanz : {}", id);
        systeminstanzRepository.deleteById(id);
        systeminstanzSearchRepository.deleteById(id);
    }

    /**
     * Search for the systeminstanz corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SysteminstanzDTO> search(String query) {
        log.debug("Request to search Systeminstanzs for query {}", query);
        return StreamSupport
            .stream(systeminstanzSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(systeminstanzMapper::toDto)
            .collect(Collectors.toList());
    }
}
