package de.kvb.eps.service;

import de.kvb.eps.domain.Hersteller;
import de.kvb.eps.repository.HerstellerRepository;
import de.kvb.eps.repository.search.HerstellerSearchRepository;
import de.kvb.eps.service.dto.HerstellerDTO;
import de.kvb.eps.service.mapper.HerstellerMapper;
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
 * Service Implementation for managing {@link Hersteller}.
 */
@Service
@Transactional
public class HerstellerService {

    private final Logger log = LoggerFactory.getLogger(HerstellerService.class);

    private final HerstellerRepository herstellerRepository;

    private final HerstellerMapper herstellerMapper;

    private final HerstellerSearchRepository herstellerSearchRepository;

    public HerstellerService(HerstellerRepository herstellerRepository, HerstellerMapper herstellerMapper, HerstellerSearchRepository herstellerSearchRepository) {
        this.herstellerRepository = herstellerRepository;
        this.herstellerMapper = herstellerMapper;
        this.herstellerSearchRepository = herstellerSearchRepository;
    }

    /**
     * Save a hersteller.
     *
     * @param herstellerDTO the entity to save.
     * @return the persisted entity.
     */
    public HerstellerDTO save(HerstellerDTO herstellerDTO) {
        log.debug("Request to save Hersteller : {}", herstellerDTO);
        Hersteller hersteller = herstellerMapper.toEntity(herstellerDTO);
        hersteller = herstellerRepository.save(hersteller);
        HerstellerDTO result = herstellerMapper.toDto(hersteller);
        herstellerSearchRepository.save(hersteller);
        return result;
    }

    /**
     * Get all the herstellers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HerstellerDTO> findAll() {
        log.debug("Request to get all Herstellers");
        return herstellerRepository.findAll().stream()
            .map(herstellerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one hersteller by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HerstellerDTO> findOne(Long id) {
        log.debug("Request to get Hersteller : {}", id);
        return herstellerRepository.findById(id)
            .map(herstellerMapper::toDto);
    }

    /**
     * Delete the hersteller by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hersteller : {}", id);
        herstellerRepository.deleteById(id);
        herstellerSearchRepository.deleteById(id);
    }

    /**
     * Search for the hersteller corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HerstellerDTO> search(String query) {
        log.debug("Request to search Herstellers for query {}", query);
        return StreamSupport
            .stream(herstellerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(herstellerMapper::toDto)
            .collect(Collectors.toList());
    }
}
