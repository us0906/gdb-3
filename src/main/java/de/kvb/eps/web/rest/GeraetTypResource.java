package de.kvb.eps.web.rest;

import de.kvb.eps.service.GeraetTypService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.GeraetTypDTO;

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
 * REST controller for managing {@link de.kvb.eps.domain.GeraetTyp}.
 */
@RestController
@RequestMapping("/api")
public class GeraetTypResource {

    private final Logger log = LoggerFactory.getLogger(GeraetTypResource.class);

    private static final String ENTITY_NAME = "geraetTyp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeraetTypService geraetTypService;

    public GeraetTypResource(GeraetTypService geraetTypService) {
        this.geraetTypService = geraetTypService;
    }

    /**
     * {@code POST  /geraet-typs} : Create a new geraetTyp.
     *
     * @param geraetTypDTO the geraetTypDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geraetTypDTO, or with status {@code 400 (Bad Request)} if the geraetTyp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/geraet-typs")
    public ResponseEntity<GeraetTypDTO> createGeraetTyp(@Valid @RequestBody GeraetTypDTO geraetTypDTO) throws URISyntaxException {
        log.debug("REST request to save GeraetTyp : {}", geraetTypDTO);
        if (geraetTypDTO.getId() != null) {
            throw new BadRequestAlertException("A new geraetTyp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeraetTypDTO result = geraetTypService.save(geraetTypDTO);
        return ResponseEntity.created(new URI("/api/geraet-typs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /geraet-typs} : Updates an existing geraetTyp.
     *
     * @param geraetTypDTO the geraetTypDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geraetTypDTO,
     * or with status {@code 400 (Bad Request)} if the geraetTypDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geraetTypDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/geraet-typs")
    public ResponseEntity<GeraetTypDTO> updateGeraetTyp(@Valid @RequestBody GeraetTypDTO geraetTypDTO) throws URISyntaxException {
        log.debug("REST request to update GeraetTyp : {}", geraetTypDTO);
        if (geraetTypDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeraetTypDTO result = geraetTypService.save(geraetTypDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geraetTypDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /geraet-typs} : get all the geraetTyps.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of geraetTyps in body.
     */
    @GetMapping("/geraet-typs")
    public List<GeraetTypDTO> getAllGeraetTyps() {
        log.debug("REST request to get all GeraetTyps");
        return geraetTypService.findAll();
    }

    /**
     * {@code GET  /geraet-typs/:id} : get the "id" geraetTyp.
     *
     * @param id the id of the geraetTypDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geraetTypDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/geraet-typs/{id}")
    public ResponseEntity<GeraetTypDTO> getGeraetTyp(@PathVariable Long id) {
        log.debug("REST request to get GeraetTyp : {}", id);
        Optional<GeraetTypDTO> geraetTypDTO = geraetTypService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geraetTypDTO);
    }

    /**
     * {@code DELETE  /geraet-typs/:id} : delete the "id" geraetTyp.
     *
     * @param id the id of the geraetTypDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/geraet-typs/{id}")
    public ResponseEntity<Void> deleteGeraetTyp(@PathVariable Long id) {
        log.debug("REST request to delete GeraetTyp : {}", id);
        geraetTypService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/geraet-typs?query=:query} : search for the geraetTyp corresponding
     * to the query.
     *
     * @param query the query of the geraetTyp search.
     * @return the result of the search.
     */
    @GetMapping("/_search/geraet-typs")
    public List<GeraetTypDTO> searchGeraetTyps(@RequestParam String query) {
        log.debug("REST request to search GeraetTyps for query {}", query);
        return geraetTypService.search(query);
    }
}
