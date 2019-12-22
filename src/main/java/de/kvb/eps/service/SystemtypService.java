package de.kvb.eps.service;

import de.kvb.eps.domain.Systemtyp;
import de.kvb.eps.repository.SystemtypRepository;
import de.kvb.eps.repository.search.SystemtypSearchRepository;
import de.kvb.eps.service.dto.SystemtypDTO;
import de.kvb.eps.service.mapper.SystemtypMapper;
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
 * Service Implementation for managing {@link Systemtyp}.
 */
@Service
@Transactional
public class SystemtypService {

    private final Logger log = LoggerFactory.getLogger(SystemtypService.class);

    private final SystemtypRepository systemtypRepository;

    private final SystemtypMapper systemtypMapper;

    private final SystemtypSearchRepository systemtypSearchRepository;

    public SystemtypService(SystemtypRepository systemtypRepository, SystemtypMapper systemtypMapper, SystemtypSearchRepository systemtypSearchRepository) {
        this.systemtypRepository = systemtypRepository;
        this.systemtypMapper = systemtypMapper;
        this.systemtypSearchRepository = systemtypSearchRepository;
    }

    /**
     * Save a systemtyp.
     *
     * @param systemtypDTO the entity to save.
     * @return the persisted entity.
     */
    public SystemtypDTO save(SystemtypDTO systemtypDTO) {
        log.debug("Request to save Systemtyp : {}", systemtypDTO);
        Systemtyp systemtyp = systemtypMapper.toEntity(systemtypDTO);
        systemtyp = systemtypRepository.save(systemtyp);
        SystemtypDTO result = systemtypMapper.toDto(systemtyp);
        systemtypSearchRepository.save(systemtyp);
        return result;
    }

    /**
     * Get all the systemtyps.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SystemtypDTO> findAll() {
        log.debug("Request to get all Systemtyps");
        return systemtypRepository.findAll().stream()
            .map(systemtypMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one systemtyp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SystemtypDTO> findOne(Long id) {
        log.debug("Request to get Systemtyp : {}", id);
        return systemtypRepository.findById(id)
            .map(systemtypMapper::toDto);
    }

    /**
     * Delete the systemtyp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Systemtyp : {}", id);
        systemtypRepository.deleteById(id);
        systemtypSearchRepository.deleteById(id);
    }

    /**
     * Search for the systemtyp corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SystemtypDTO> search(String query) {
        log.debug("Request to search Systemtyps for query {}", query);
        return StreamSupport
            .stream(systemtypSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(systemtypMapper::toDto)
            .collect(Collectors.toList());
    }
}
