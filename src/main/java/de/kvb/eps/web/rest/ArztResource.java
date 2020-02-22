package de.kvb.eps.web.rest;

import de.kvb.eps.service.ArztService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.ArztDTO;
import de.kvb.eps.service.dto.ArztCriteria;
import de.kvb.eps.service.ArztQueryService;

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
 * REST controller for managing {@link de.kvb.eps.domain.Arzt}.
 */
@RestController
@RequestMapping("/api")
public class ArztResource {

    private final Logger log = LoggerFactory.getLogger(ArztResource.class);

    private static final String ENTITY_NAME = "arzt";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArztService arztService;

    private final ArztQueryService arztQueryService;

    public ArztResource(ArztService arztService, ArztQueryService arztQueryService) {
        this.arztService = arztService;
        this.arztQueryService = arztQueryService;
    }

    /**
     * {@code POST  /arzts} : Create a new arzt.
     *
     * @param arztDTO the arztDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new arztDTO, or with status {@code 400 (Bad Request)} if the arzt has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/arzts")
    public ResponseEntity<ArztDTO> createArzt(@Valid @RequestBody ArztDTO arztDTO) throws URISyntaxException {
        log.debug("REST request to save Arzt : {}", arztDTO);
        if (arztDTO.getId() != null) {
            throw new BadRequestAlertException("A new arzt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArztDTO result = arztService.save(arztDTO);
        return ResponseEntity.created(new URI("/api/arzts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /arzts} : Updates an existing arzt.
     *
     * @param arztDTO the arztDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated arztDTO,
     * or with status {@code 400 (Bad Request)} if the arztDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the arztDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/arzts")
    public ResponseEntity<ArztDTO> updateArzt(@Valid @RequestBody ArztDTO arztDTO) throws URISyntaxException {
        log.debug("REST request to update Arzt : {}", arztDTO);
        if (arztDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArztDTO result = arztService.save(arztDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, arztDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /arzts} : get all the arzts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of arzts in body.
     */
    @GetMapping("/arzts")
    public ResponseEntity<List<ArztDTO>> getAllArzts(ArztCriteria criteria) {
        log.debug("REST request to get Arzts by criteria: {}", criteria);
        List<ArztDTO> entityList = arztQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /arzts/count} : count all the arzts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/arzts/count")
    public ResponseEntity<Long> countArzts(ArztCriteria criteria) {
        log.debug("REST request to count Arzts by criteria: {}", criteria);
        return ResponseEntity.ok().body(arztQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /arzts/:id} : get the "id" arzt.
     *
     * @param id the id of the arztDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the arztDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/arzts/{id}")
    public ResponseEntity<ArztDTO> getArzt(@PathVariable Long id) {
        log.debug("REST request to get Arzt : {}", id);
        Optional<ArztDTO> arztDTO = arztService.findOne(id);
        return ResponseUtil.wrapOrNotFound(arztDTO);
    }

    /**
     * {@code DELETE  /arzts/:id} : delete the "id" arzt.
     *
     * @param id the id of the arztDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/arzts/{id}")
    public ResponseEntity<Void> deleteArzt(@PathVariable Long id) {
        log.debug("REST request to delete Arzt : {}", id);
        arztService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/arzts?query=:query} : search for the arzt corresponding
     * to the query.
     *
     * @param query the query of the arzt search.
     * @return the result of the search.
     */
    @GetMapping("/_search/arzts")
    public List<ArztDTO> searchArzts(@RequestParam String query) {
        log.debug("REST request to search Arzts for query {}", query);
        return arztService.search(query);
    }
}
