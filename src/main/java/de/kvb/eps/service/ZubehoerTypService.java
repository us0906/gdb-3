package de.kvb.eps.service;

import de.kvb.eps.domain.ZubehoerTyp;
import de.kvb.eps.repository.ZubehoerTypRepository;
import de.kvb.eps.repository.search.ZubehoerTypSearchRepository;
import de.kvb.eps.service.dto.ZubehoerTypDTO;
import de.kvb.eps.service.mapper.ZubehoerTypMapper;
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
 * Service Implementation for managing {@link ZubehoerTyp}.
 */
@Service
@Transactional
public class ZubehoerTypService {

    private final Logger log = LoggerFactory.getLogger(ZubehoerTypService.class);

    private final ZubehoerTypRepository zubehoerTypRepository;

    private final ZubehoerTypMapper zubehoerTypMapper;

    private final ZubehoerTypSearchRepository zubehoerTypSearchRepository;

    public ZubehoerTypService(ZubehoerTypRepository zubehoerTypRepository, ZubehoerTypMapper zubehoerTypMapper, ZubehoerTypSearchRepository zubehoerTypSearchRepository) {
        this.zubehoerTypRepository = zubehoerTypRepository;
        this.zubehoerTypMapper = zubehoerTypMapper;
        this.zubehoerTypSearchRepository = zubehoerTypSearchRepository;
    }

    /**
     * Save a zubehoerTyp.
     *
     * @param zubehoerTypDTO the entity to save.
     * @return the persisted entity.
     */
    public ZubehoerTypDTO save(ZubehoerTypDTO zubehoerTypDTO) {
        log.debug("Request to save ZubehoerTyp : {}", zubehoerTypDTO);
        ZubehoerTyp zubehoerTyp = zubehoerTypMapper.toEntity(zubehoerTypDTO);
        zubehoerTyp = zubehoerTypRepository.save(zubehoerTyp);
        ZubehoerTypDTO result = zubehoerTypMapper.toDto(zubehoerTyp);
        zubehoerTypSearchRepository.save(zubehoerTyp);
        return result;
    }

    /**
     * Get all the zubehoerTyps.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ZubehoerTypDTO> findAll() {
        log.debug("Request to get all ZubehoerTyps");
        return zubehoerTypRepository.findAll().stream()
            .map(zubehoerTypMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one zubehoerTyp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ZubehoerTypDTO> findOne(Long id) {
        log.debug("Request to get ZubehoerTyp : {}", id);
        return zubehoerTypRepository.findById(id)
            .map(zubehoerTypMapper::toDto);
    }

    /**
     * Delete the zubehoerTyp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ZubehoerTyp : {}", id);
        zubehoerTypRepository.deleteById(id);
        zubehoerTypSearchRepository.deleteById(id);
    }

    /**
     * Search for the zubehoerTyp corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ZubehoerTypDTO> search(String query) {
        log.debug("Request to search ZubehoerTyps for query {}", query);
        return StreamSupport
            .stream(zubehoerTypSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(zubehoerTypMapper::toDto)
            .collect(Collectors.toList());
    }
}
