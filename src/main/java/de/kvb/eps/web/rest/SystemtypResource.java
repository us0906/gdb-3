package de.kvb.eps.web.rest;

import de.kvb.eps.service.SystemtypService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.SystemtypDTO;
import de.kvb.eps.service.dto.SystemtypCriteria;
import de.kvb.eps.service.SystemtypQueryService;

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
 * REST controller for managing {@link de.kvb.eps.domain.Systemtyp}.
 */
@RestController
@RequestMapping("/api")
public class SystemtypResource {

    private final Logger log = LoggerFactory.getLogger(SystemtypResource.class);

    private static final String ENTITY_NAME = "systemtyp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemtypService systemtypService;

    private final SystemtypQueryService systemtypQueryService;

    public SystemtypResource(SystemtypService systemtypService, SystemtypQueryService systemtypQueryService) {
        this.systemtypService = systemtypService;
        this.systemtypQueryService = systemtypQueryService;
    }

    /**
     * {@code POST  /systemtyps} : Create a new systemtyp.
     *
     * @param systemtypDTO the systemtypDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemtypDTO, or with status {@code 400 (Bad Request)} if the systemtyp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/systemtyps")
    public ResponseEntity<SystemtypDTO> createSystemtyp(@Valid @RequestBody SystemtypDTO systemtypDTO) throws URISyntaxException {
        log.debug("REST request to save Systemtyp : {}", systemtypDTO);
        if (systemtypDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemtyp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemtypDTO result = systemtypService.save(systemtypDTO);
        return ResponseEntity.created(new URI("/api/systemtyps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /systemtyps} : Updates an existing systemtyp.
     *
     * @param systemtypDTO the systemtypDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemtypDTO,
     * or with status {@code 400 (Bad Request)} if the systemtypDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemtypDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/systemtyps")
    public ResponseEntity<SystemtypDTO> updateSystemtyp(@Valid @RequestBody SystemtypDTO systemtypDTO) throws URISyntaxException {
        log.debug("REST request to update Systemtyp : {}", systemtypDTO);
        if (systemtypDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SystemtypDTO result = systemtypService.save(systemtypDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, systemtypDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /systemtyps} : get all the systemtyps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemtyps in body.
     */
    @GetMapping("/systemtyps")
    public ResponseEntity<List<SystemtypDTO>> getAllSystemtyps(SystemtypCriteria criteria) {
        log.debug("REST request to get Systemtyps by criteria: {}", criteria);
        List<SystemtypDTO> entityList = systemtypQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /systemtyps/count} : count all the systemtyps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/systemtyps/count")
    public ResponseEntity<Long> countSystemtyps(SystemtypCriteria criteria) {
        log.debug("REST request to count Systemtyps by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemtypQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /systemtyps/:id} : get the "id" systemtyp.
     *
     * @param id the id of the systemtypDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemtypDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/systemtyps/{id}")
    public ResponseEntity<SystemtypDTO> getSystemtyp(@PathVariable Long id) {
        log.debug("REST request to get Systemtyp : {}", id);
        Optional<SystemtypDTO> systemtypDTO = systemtypService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemtypDTO);
    }

    /**
     * {@code DELETE  /systemtyps/:id} : delete the "id" systemtyp.
     *
     * @param id the id of the systemtypDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/systemtyps/{id}")
    public ResponseEntity<Void> deleteSystemtyp(@PathVariable Long id) {
        log.debug("REST request to delete Systemtyp : {}", id);
        systemtypService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/systemtyps?query=:query} : search for the systemtyp corresponding
     * to the query.
     *
     * @param query the query of the systemtyp search.
     * @return the result of the search.
     */
    @GetMapping("/_search/systemtyps")
    public List<SystemtypDTO> searchSystemtyps(@RequestParam String query) {
        log.debug("REST request to search Systemtyps for query {}", query);
        return systemtypService.search(query);
    }
}
