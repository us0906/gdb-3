package de.kvb.eps.web.rest;

import de.kvb.eps.service.GeraetService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.GeraetDTO;
import de.kvb.eps.service.dto.GeraetCriteria;
import de.kvb.eps.service.GeraetQueryService;

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
 * REST controller for managing {@link de.kvb.eps.domain.Geraet}.
 */
@RestController
@RequestMapping("/api")
public class GeraetResource {

    private final Logger log = LoggerFactory.getLogger(GeraetResource.class);

    private static final String ENTITY_NAME = "geraet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GeraetService geraetService;

    private final GeraetQueryService geraetQueryService;

    public GeraetResource(GeraetService geraetService, GeraetQueryService geraetQueryService) {
        this.geraetService = geraetService;
        this.geraetQueryService = geraetQueryService;
    }

    /**
     * {@code POST  /geraets} : Create a new geraet.
     *
     * @param geraetDTO the geraetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new geraetDTO, or with status {@code 400 (Bad Request)} if the geraet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/geraets")
    public ResponseEntity<GeraetDTO> createGeraet(@Valid @RequestBody GeraetDTO geraetDTO) throws URISyntaxException {
        log.debug("REST request to save Geraet : {}", geraetDTO);
        if (geraetDTO.getId() != null) {
            throw new BadRequestAlertException("A new geraet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GeraetDTO result = geraetService.save(geraetDTO);
        return ResponseEntity.created(new URI("/api/geraets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /geraets} : Updates an existing geraet.
     *
     * @param geraetDTO the geraetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated geraetDTO,
     * or with status {@code 400 (Bad Request)} if the geraetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the geraetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/geraets")
    public ResponseEntity<GeraetDTO> updateGeraet(@Valid @RequestBody GeraetDTO geraetDTO) throws URISyntaxException {
        log.debug("REST request to update Geraet : {}", geraetDTO);
        if (geraetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GeraetDTO result = geraetService.save(geraetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, geraetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /geraets} : get all the geraets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of geraets in body.
     */
    @GetMapping("/geraets")
    public ResponseEntity<List<GeraetDTO>> getAllGeraets(GeraetCriteria criteria) {
        log.debug("REST request to get Geraets by criteria: {}", criteria);
        List<GeraetDTO> entityList = geraetQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /geraets/count} : count all the geraets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/geraets/count")
    public ResponseEntity<Long> countGeraets(GeraetCriteria criteria) {
        log.debug("REST request to count Geraets by criteria: {}", criteria);
        return ResponseEntity.ok().body(geraetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /geraets/:id} : get the "id" geraet.
     *
     * @param id the id of the geraetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the geraetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/geraets/{id}")
    public ResponseEntity<GeraetDTO> getGeraet(@PathVariable Long id) {
        log.debug("REST request to get Geraet : {}", id);
        Optional<GeraetDTO> geraetDTO = geraetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(geraetDTO);
    }

    /**
     * {@code DELETE  /geraets/:id} : delete the "id" geraet.
     *
     * @param id the id of the geraetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/geraets/{id}")
    public ResponseEntity<Void> deleteGeraet(@PathVariable Long id) {
        log.debug("REST request to delete Geraet : {}", id);
        geraetService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/geraets?query=:query} : search for the geraet corresponding
     * to the query.
     *
     * @param query the query of the geraet search.
     * @return the result of the search.
     */
    @GetMapping("/_search/geraets")
    public List<GeraetDTO> searchGeraets(@RequestParam String query) {
        log.debug("REST request to search Geraets for query {}", query);
        return geraetService.search(query);
    }
}
