package de.kvb.eps.web.rest;

import de.kvb.eps.service.BetriebsstaetteService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.BetriebsstaetteDTO;
import de.kvb.eps.service.dto.BetriebsstaetteCriteria;
import de.kvb.eps.service.BetriebsstaetteQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link de.kvb.eps.domain.Betriebsstaette}.
 */
@RestController
@RequestMapping("/api")
public class BetriebsstaetteResource {

    private final Logger log = LoggerFactory.getLogger(BetriebsstaetteResource.class);

    private static final String ENTITY_NAME = "betriebsstaette";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BetriebsstaetteService betriebsstaetteService;

    private final BetriebsstaetteQueryService betriebsstaetteQueryService;

    public BetriebsstaetteResource(BetriebsstaetteService betriebsstaetteService, BetriebsstaetteQueryService betriebsstaetteQueryService) {
        this.betriebsstaetteService = betriebsstaetteService;
        this.betriebsstaetteQueryService = betriebsstaetteQueryService;
    }

    /**
     * {@code POST  /betriebsstaettes} : Create a new betriebsstaette.
     *
     * @param betriebsstaetteDTO the betriebsstaetteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new betriebsstaetteDTO, or with status {@code 400 (Bad Request)} if the betriebsstaette has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/betriebsstaettes")
    public ResponseEntity<BetriebsstaetteDTO> createBetriebsstaette(@RequestBody BetriebsstaetteDTO betriebsstaetteDTO) throws URISyntaxException {
        log.debug("REST request to save Betriebsstaette : {}", betriebsstaetteDTO);
        if (betriebsstaetteDTO.getId() != null) {
            throw new BadRequestAlertException("A new betriebsstaette cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BetriebsstaetteDTO result = betriebsstaetteService.save(betriebsstaetteDTO);
        return ResponseEntity.created(new URI("/api/betriebsstaettes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /betriebsstaettes} : Updates an existing betriebsstaette.
     *
     * @param betriebsstaetteDTO the betriebsstaetteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated betriebsstaetteDTO,
     * or with status {@code 400 (Bad Request)} if the betriebsstaetteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the betriebsstaetteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/betriebsstaettes")
    public ResponseEntity<BetriebsstaetteDTO> updateBetriebsstaette(@RequestBody BetriebsstaetteDTO betriebsstaetteDTO) throws URISyntaxException {
        log.debug("REST request to update Betriebsstaette : {}", betriebsstaetteDTO);
        if (betriebsstaetteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BetriebsstaetteDTO result = betriebsstaetteService.save(betriebsstaetteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, betriebsstaetteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /betriebsstaettes} : get all the betriebsstaettes.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of betriebsstaettes in body.
     */
    @GetMapping("/betriebsstaettes")
    public ResponseEntity<List<BetriebsstaetteDTO>> getAllBetriebsstaettes(BetriebsstaetteCriteria criteria) {
        log.debug("REST request to get Betriebsstaettes by criteria: {}", criteria);
        List<BetriebsstaetteDTO> entityList = betriebsstaetteQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /betriebsstaettes/count} : count all the betriebsstaettes.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/betriebsstaettes/count")
    public ResponseEntity<Long> countBetriebsstaettes(BetriebsstaetteCriteria criteria) {
        log.debug("REST request to count Betriebsstaettes by criteria: {}", criteria);
        return ResponseEntity.ok().body(betriebsstaetteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /betriebsstaettes/:id} : get the "id" betriebsstaette.
     *
     * @param id the id of the betriebsstaetteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the betriebsstaetteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/betriebsstaettes/{id}")
    public ResponseEntity<BetriebsstaetteDTO> getBetriebsstaette(@PathVariable Long id) {
        log.debug("REST request to get Betriebsstaette : {}", id);
        Optional<BetriebsstaetteDTO> betriebsstaetteDTO = betriebsstaetteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(betriebsstaetteDTO);
    }

    /**
     * {@code DELETE  /betriebsstaettes/:id} : delete the "id" betriebsstaette.
     *
     * @param id the id of the betriebsstaetteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/betriebsstaettes/{id}")
    public ResponseEntity<Void> deleteBetriebsstaette(@PathVariable Long id) {
        log.debug("REST request to delete Betriebsstaette : {}", id);
        betriebsstaetteService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/betriebsstaettes?query=:query} : search for the betriebsstaette corresponding
     * to the query.
     *
     * @param query the query of the betriebsstaette search.
     * @return the result of the search.
     */
    @GetMapping("/_search/betriebsstaettes")
    public List<BetriebsstaetteDTO> searchBetriebsstaettes(@RequestParam String query) {
        log.debug("REST request to search Betriebsstaettes for query {}", query);
        return betriebsstaetteService.search(query);
    }
}
