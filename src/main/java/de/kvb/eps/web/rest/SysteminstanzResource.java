package de.kvb.eps.web.rest;

import de.kvb.eps.service.SysteminstanzService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.SysteminstanzDTO;
import de.kvb.eps.service.dto.SysteminstanzCriteria;
import de.kvb.eps.service.SysteminstanzQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link de.kvb.eps.domain.Systeminstanz}.
 */
@RestController
@RequestMapping("/api")
public class SysteminstanzResource {

    private final Logger log = LoggerFactory.getLogger(SysteminstanzResource.class);

    private static final String ENTITY_NAME = "systeminstanz";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SysteminstanzService systeminstanzService;

    private final SysteminstanzQueryService systeminstanzQueryService;

    public SysteminstanzResource(SysteminstanzService systeminstanzService, SysteminstanzQueryService systeminstanzQueryService) {
        this.systeminstanzService = systeminstanzService;
        this.systeminstanzQueryService = systeminstanzQueryService;
    }

    /**
     * {@code POST  /systeminstanzs} : Create a new systeminstanz.
     *
     * @param systeminstanzDTO the systeminstanzDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systeminstanzDTO, or with status {@code 400 (Bad Request)} if the systeminstanz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/systeminstanzs")
    public ResponseEntity<SysteminstanzDTO> createSysteminstanz(@Valid @RequestBody SysteminstanzDTO systeminstanzDTO) throws URISyntaxException {
        log.debug("REST request to save Systeminstanz : {}", systeminstanzDTO);
        if (systeminstanzDTO.getId() != null) {
            throw new BadRequestAlertException("A new systeminstanz cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SysteminstanzDTO result = systeminstanzService.save(systeminstanzDTO);
        return ResponseEntity.created(new URI("/api/systeminstanzs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /systeminstanzs} : Updates an existing systeminstanz.
     *
     * @param systeminstanzDTO the systeminstanzDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systeminstanzDTO,
     * or with status {@code 400 (Bad Request)} if the systeminstanzDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systeminstanzDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/systeminstanzs")
    public ResponseEntity<SysteminstanzDTO> updateSysteminstanz(@Valid @RequestBody SysteminstanzDTO systeminstanzDTO) throws URISyntaxException {
        log.debug("REST request to update Systeminstanz : {}", systeminstanzDTO);
        if (systeminstanzDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SysteminstanzDTO result = systeminstanzService.save(systeminstanzDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systeminstanzDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /systeminstanzs} : get all the systeminstanzs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systeminstanzs in body.
     */
    @GetMapping("/systeminstanzs")
    public ResponseEntity<List<SysteminstanzDTO>> getAllSysteminstanzs(SysteminstanzCriteria criteria) {
        log.debug("REST request to get Systeminstanzs by criteria: {}", criteria);
        List<SysteminstanzDTO> entityList = systeminstanzQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /systeminstanzs/count} : count all the systeminstanzs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/systeminstanzs/count")
    public ResponseEntity<Long> countSysteminstanzs(SysteminstanzCriteria criteria) {
        log.debug("REST request to count Systeminstanzs by criteria: {}", criteria);
        return ResponseEntity.ok().body(systeminstanzQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /systeminstanzs/:id} : get the "id" systeminstanz.
     *
     * @param id the id of the systeminstanzDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systeminstanzDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/systeminstanzs/{id}")
    public ResponseEntity<SysteminstanzDTO> getSysteminstanz(@PathVariable Long id) {
        log.debug("REST request to get Systeminstanz : {}", id);
        Optional<SysteminstanzDTO> systeminstanzDTO = systeminstanzService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systeminstanzDTO);
    }

    /**
     * {@code DELETE  /systeminstanzs/:id} : delete the "id" systeminstanz.
     *
     * @param id the id of the systeminstanzDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/systeminstanzs/{id}")
    public ResponseEntity<Void> deleteSysteminstanz(@PathVariable Long id) {
        log.debug("REST request to delete Systeminstanz : {}", id);
        systeminstanzService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/systeminstanzs?query=:query} : search for the systeminstanz corresponding
     * to the query.
     *
     * @param query the query of the systeminstanz search.
     * @return the result of the search.
     */
    @GetMapping("/_search/systeminstanzs")
    public List<SysteminstanzDTO> searchSysteminstanzs(@RequestParam String query) {
        log.debug("REST request to search Systeminstanzs for query {}", query);
        return systeminstanzService.search(query);
    }
}
