package de.kvb.eps.web.rest;

import de.kvb.eps.service.SystemnutzungService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.SystemnutzungDTO;
import de.kvb.eps.service.dto.SystemnutzungCriteria;
import de.kvb.eps.service.SystemnutzungQueryService;

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
 * REST controller for managing {@link de.kvb.eps.domain.Systemnutzung}.
 */
@RestController
@RequestMapping("/api")
public class SystemnutzungResource {

    private final Logger log = LoggerFactory.getLogger(SystemnutzungResource.class);

    private static final String ENTITY_NAME = "systemnutzung";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemnutzungService systemnutzungService;

    private final SystemnutzungQueryService systemnutzungQueryService;

    public SystemnutzungResource(SystemnutzungService systemnutzungService, SystemnutzungQueryService systemnutzungQueryService) {
        this.systemnutzungService = systemnutzungService;
        this.systemnutzungQueryService = systemnutzungQueryService;
    }

    /**
     * {@code POST  /systemnutzungs} : Create a new systemnutzung.
     *
     * @param systemnutzungDTO the systemnutzungDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemnutzungDTO, or with status {@code 400 (Bad Request)} if the systemnutzung has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/systemnutzungs")
    public ResponseEntity<SystemnutzungDTO> createSystemnutzung(@Valid @RequestBody SystemnutzungDTO systemnutzungDTO) throws URISyntaxException {
        log.debug("REST request to save Systemnutzung : {}", systemnutzungDTO);
        if (systemnutzungDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemnutzung cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemnutzungDTO result = systemnutzungService.save(systemnutzungDTO);
        return ResponseEntity.created(new URI("/api/systemnutzungs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /systemnutzungs} : Updates an existing systemnutzung.
     *
     * @param systemnutzungDTO the systemnutzungDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemnutzungDTO,
     * or with status {@code 400 (Bad Request)} if the systemnutzungDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemnutzungDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/systemnutzungs")
    public ResponseEntity<SystemnutzungDTO> updateSystemnutzung(@Valid @RequestBody SystemnutzungDTO systemnutzungDTO) throws URISyntaxException {
        log.debug("REST request to update Systemnutzung : {}", systemnutzungDTO);
        if (systemnutzungDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SystemnutzungDTO result = systemnutzungService.save(systemnutzungDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemnutzungDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /systemnutzungs} : get all the systemnutzungs.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemnutzungs in body.
     */
    @GetMapping("/systemnutzungs")
    public ResponseEntity<List<SystemnutzungDTO>> getAllSystemnutzungs(SystemnutzungCriteria criteria) {
        log.debug("REST request to get Systemnutzungs by criteria: {}", criteria);
        List<SystemnutzungDTO> entityList = systemnutzungQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /systemnutzungs/count} : count all the systemnutzungs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/systemnutzungs/count")
    public ResponseEntity<Long> countSystemnutzungs(SystemnutzungCriteria criteria) {
        log.debug("REST request to count Systemnutzungs by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemnutzungQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /systemnutzungs/:id} : get the "id" systemnutzung.
     *
     * @param id the id of the systemnutzungDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemnutzungDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/systemnutzungs/{id}")
    public ResponseEntity<SystemnutzungDTO> getSystemnutzung(@PathVariable Long id) {
        log.debug("REST request to get Systemnutzung : {}", id);
        Optional<SystemnutzungDTO> systemnutzungDTO = systemnutzungService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemnutzungDTO);
    }

    /**
     * {@code DELETE  /systemnutzungs/:id} : delete the "id" systemnutzung.
     *
     * @param id the id of the systemnutzungDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/systemnutzungs/{id}")
    public ResponseEntity<Void> deleteSystemnutzung(@PathVariable Long id) {
        log.debug("REST request to delete Systemnutzung : {}", id);
        systemnutzungService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/systemnutzungs?query=:query} : search for the systemnutzung corresponding
     * to the query.
     *
     * @param query the query of the systemnutzung search.
     * @return the result of the search.
     */
    @GetMapping("/_search/systemnutzungs")
    public List<SystemnutzungDTO> searchSystemnutzungs(@RequestParam String query) {
        log.debug("REST request to search Systemnutzungs for query {}", query);
        return systemnutzungService.search(query);
    }
}
