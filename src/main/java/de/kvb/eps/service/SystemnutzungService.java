package de.kvb.eps.service;

import de.kvb.eps.domain.Systemnutzung;
import de.kvb.eps.repository.SystemnutzungRepository;
import de.kvb.eps.repository.search.SystemnutzungSearchRepository;
import de.kvb.eps.service.dto.SystemnutzungDTO;
import de.kvb.eps.service.mapper.SystemnutzungMapper;
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
 * Service Implementation for managing {@link Systemnutzung}.
 */
@Service
@Transactional
public class SystemnutzungService {

    private final Logger log = LoggerFactory.getLogger(SystemnutzungService.class);

    private final SystemnutzungRepository systemnutzungRepository;

    private final SystemnutzungMapper systemnutzungMapper;

    private final SystemnutzungSearchRepository systemnutzungSearchRepository;

    public SystemnutzungService(SystemnutzungRepository systemnutzungRepository, SystemnutzungMapper systemnutzungMapper, SystemnutzungSearchRepository systemnutzungSearchRepository) {
        this.systemnutzungRepository = systemnutzungRepository;
        this.systemnutzungMapper = systemnutzungMapper;
        this.systemnutzungSearchRepository = systemnutzungSearchRepository;
    }

    /**
     * Save a systemnutzung.
     *
     * @param systemnutzungDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemnutzungDTO save(SystemnutzungDTO systemnutzungDTO) {
        log.debug("Request to save Systemnutzung : {}", systemnutzungDTO);
        Systemnutzung systemnutzung = systemnutzungMapper.toEntity(systemnutzungDTO);
        systemnutzung = systemnutzungRepository.save(systemnutzung);
        SystemnutzungDTO result = systemnutzungMapper.toDto(systemnutzung);
        systemnutzungSearchRepository.save(systemnutzung);
        return result;
    }

    /**
     * Get all the systemnutzungs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SystemnutzungDTO> findAll() {
        log.debug("Request to get all Systemnutzungs");
        return systemnutzungRepository.findAll().stream()
            .map(systemnutzungMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one systemnutzung by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemnutzungDTO> findOne(Long id) {
        log.debug("Request to get Systemnutzung : {}", id);
        return systemnutzungRepository.findById(id)
            .map(systemnutzungMapper::toDto);
    }

    /**
     * Delete the systemnutzung by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Systemnutzung : {}", id);
        systemnutzungRepository.deleteById(id);
        systemnutzungSearchRepository.deleteById(id);
    }

    /**
     * Search for the systemnutzung corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SystemnutzungDTO> search(String query) {
        log.debug("Request to search Systemnutzungs for query {}", query);
        return StreamSupport
            .stream(systemnutzungSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(systemnutzungMapper::toDto)
            .collect(Collectors.toList());
    }
}
