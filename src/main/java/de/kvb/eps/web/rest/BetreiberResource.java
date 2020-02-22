package de.kvb.eps.web.rest;

import de.kvb.eps.service.BetreiberService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.BetreiberDTO;
import de.kvb.eps.service.dto.BetreiberCriteria;
import de.kvb.eps.service.BetreiberQueryService;

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
 * REST controller for managing {@link de.kvb.eps.domain.Betreiber}.
 */
@RestController
@RequestMapping("/api")
public class BetreiberResource {

    private final Logger log = LoggerFactory.getLogger(BetreiberResource.class);

    private static final String ENTITY_NAME = "betreiber";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BetreiberService betreiberService;

    private final BetreiberQueryService betreiberQueryService;

    public BetreiberResource(BetreiberService betreiberService, BetreiberQueryService betreiberQueryService) {
        this.betreiberService = betreiberService;
        this.betreiberQueryService = betreiberQueryService;
    }

    /**
     * {@code POST  /betreibers} : Create a new betreiber.
     *
     * @param betreiberDTO the betreiberDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new betreiberDTO, or with status {@code 400 (Bad Request)} if the betreiber has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/betreibers")
    public ResponseEntity<BetreiberDTO> createBetreiber(@Valid @RequestBody BetreiberDTO betreiberDTO) throws URISyntaxException {
        log.debug("REST request to save Betreiber : {}", betreiberDTO);
        if (betreiberDTO.getId() != null) {
            throw new BadRequestAlertException("A new betreiber cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BetreiberDTO result = betreiberService.save(betreiberDTO);
        return ResponseEntity.created(new URI("/api/betreibers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /betreibers} : Updates an existing betreiber.
     *
     * @param betreiberDTO the betreiberDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated betreiberDTO,
     * or with status {@code 400 (Bad Request)} if the betreiberDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the betreiberDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/betreibers")
    public ResponseEntity<BetreiberDTO> updateBetreiber(@Valid @RequestBody BetreiberDTO betreiberDTO) throws URISyntaxException {
        log.debug("REST request to update Betreiber : {}", betreiberDTO);
        if (betreiberDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BetreiberDTO result = betreiberService.save(betreiberDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, betreiberDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /betreibers} : get all the betreibers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of betreibers in body.
     */
    @GetMapping("/betreibers")
    public ResponseEntity<List<BetreiberDTO>> getAllBetreibers(BetreiberCriteria criteria) {
        log.debug("REST request to get Betreibers by criteria: {}", criteria);
        List<BetreiberDTO> entityList = betreiberQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /betreibers/count} : count all the betreibers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/betreibers/count")
    public ResponseEntity<Long> countBetreibers(BetreiberCriteria criteria) {
        log.debug("REST request to count Betreibers by criteria: {}", criteria);
        return ResponseEntity.ok().body(betreiberQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /betreibers/:id} : get the "id" betreiber.
     *
     * @param id the id of the betreiberDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the betreiberDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/betreibers/{id}")
    public ResponseEntity<BetreiberDTO> getBetreiber(@PathVariable Long id) {
        log.debug("REST request to get Betreiber : {}", id);
        Optional<BetreiberDTO> betreiberDTO = betreiberService.findOne(id);
        return ResponseUtil.wrapOrNotFound(betreiberDTO);
    }

    /**
     * {@code DELETE  /betreibers/:id} : delete the "id" betreiber.
     *
     * @param id the id of the betreiberDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/betreibers/{id}")
    public ResponseEntity<Void> deleteBetreiber(@PathVariable Long id) {
        log.debug("REST request to delete Betreiber : {}", id);
        betreiberService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/betreibers?query=:query} : search for the betreiber corresponding
     * to the query.
     *
     * @param query the query of the betreiber search.
     * @return the result of the search.
     */
    @GetMapping("/_search/betreibers")
    public List<BetreiberDTO> searchBetreibers(@RequestParam String query) {
        log.debug("REST request to search Betreibers for query {}", query);
        return betreiberService.search(query);
    }
}
