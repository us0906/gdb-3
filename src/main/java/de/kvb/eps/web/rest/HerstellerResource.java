package de.kvb.eps.web.rest;

import de.kvb.eps.service.HerstellerService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.HerstellerDTO;

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
 * REST controller for managing {@link de.kvb.eps.domain.Hersteller}.
 */
@RestController
@RequestMapping("/api")
public class HerstellerResource {

    private final Logger log = LoggerFactory.getLogger(HerstellerResource.class);

    private static final String ENTITY_NAME = "hersteller";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HerstellerService herstellerService;

    public HerstellerResource(HerstellerService herstellerService) {
        this.herstellerService = herstellerService;
    }

    /**
     * {@code POST  /herstellers} : Create a new hersteller.
     *
     * @param herstellerDTO the herstellerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new herstellerDTO, or with status {@code 400 (Bad Request)} if the hersteller has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/herstellers")
    public ResponseEntity<HerstellerDTO> createHersteller(@Valid @RequestBody HerstellerDTO herstellerDTO) throws URISyntaxException {
        log.debug("REST request to save Hersteller : {}", herstellerDTO);
        if (herstellerDTO.getId() != null) {
            throw new BadRequestAlertException("A new hersteller cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HerstellerDTO result = herstellerService.save(herstellerDTO);
        return ResponseEntity.created(new URI("/api/herstellers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /herstellers} : Updates an existing hersteller.
     *
     * @param herstellerDTO the herstellerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated herstellerDTO,
     * or with status {@code 400 (Bad Request)} if the herstellerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the herstellerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/herstellers")
    public ResponseEntity<HerstellerDTO> updateHersteller(@Valid @RequestBody HerstellerDTO herstellerDTO) throws URISyntaxException {
        log.debug("REST request to update Hersteller : {}", herstellerDTO);
        if (herstellerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HerstellerDTO result = herstellerService.save(herstellerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, herstellerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /herstellers} : get all the herstellers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of herstellers in body.
     */
    @GetMapping("/herstellers")
    public List<HerstellerDTO> getAllHerstellers() {
        log.debug("REST request to get all Herstellers");
        return herstellerService.findAll();
    }

    /**
     * {@code GET  /herstellers/:id} : get the "id" hersteller.
     *
     * @param id the id of the herstellerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the herstellerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/herstellers/{id}")
    public ResponseEntity<HerstellerDTO> getHersteller(@PathVariable Long id) {
        log.debug("REST request to get Hersteller : {}", id);
        Optional<HerstellerDTO> herstellerDTO = herstellerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(herstellerDTO);
    }

    /**
     * {@code DELETE  /herstellers/:id} : delete the "id" hersteller.
     *
     * @param id the id of the herstellerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/herstellers/{id}")
    public ResponseEntity<Void> deleteHersteller(@PathVariable Long id) {
        log.debug("REST request to delete Hersteller : {}", id);
        herstellerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/herstellers?query=:query} : search for the hersteller corresponding
     * to the query.
     *
     * @param query the query of the hersteller search.
     * @return the result of the search.
     */
    @GetMapping("/_search/herstellers")
    public List<HerstellerDTO> searchHerstellers(@RequestParam String query) {
        log.debug("REST request to search Herstellers for query {}", query);
        return herstellerService.search(query);
    }
}
