package de.kvb.eps.web.rest;

import de.kvb.eps.service.ZubehoerTypService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.ZubehoerTypDTO;
import de.kvb.eps.service.dto.ZubehoerTypCriteria;
import de.kvb.eps.service.ZubehoerTypQueryService;

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
 * REST controller for managing {@link de.kvb.eps.domain.ZubehoerTyp}.
 */
@RestController
@RequestMapping("/api")
public class ZubehoerTypResource {

    private final Logger log = LoggerFactory.getLogger(ZubehoerTypResource.class);

    private static final String ENTITY_NAME = "zubehoerTyp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ZubehoerTypService zubehoerTypService;

    private final ZubehoerTypQueryService zubehoerTypQueryService;

    public ZubehoerTypResource(ZubehoerTypService zubehoerTypService, ZubehoerTypQueryService zubehoerTypQueryService) {
        this.zubehoerTypService = zubehoerTypService;
        this.zubehoerTypQueryService = zubehoerTypQueryService;
    }

    /**
     * {@code POST  /zubehoer-typs} : Create a new zubehoerTyp.
     *
     * @param zubehoerTypDTO the zubehoerTypDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zubehoerTypDTO, or with status {@code 400 (Bad Request)} if the zubehoerTyp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/zubehoer-typs")
    public ResponseEntity<ZubehoerTypDTO> createZubehoerTyp(@Valid @RequestBody ZubehoerTypDTO zubehoerTypDTO) throws URISyntaxException {
        log.debug("REST request to save ZubehoerTyp : {}", zubehoerTypDTO);
        if (zubehoerTypDTO.getId() != null) {
            throw new BadRequestAlertException("A new zubehoerTyp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ZubehoerTypDTO result = zubehoerTypService.save(zubehoerTypDTO);
        return ResponseEntity.created(new URI("/api/zubehoer-typs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /zubehoer-typs} : Updates an existing zubehoerTyp.
     *
     * @param zubehoerTypDTO the zubehoerTypDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zubehoerTypDTO,
     * or with status {@code 400 (Bad Request)} if the zubehoerTypDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zubehoerTypDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/zubehoer-typs")
    public ResponseEntity<ZubehoerTypDTO> updateZubehoerTyp(@Valid @RequestBody ZubehoerTypDTO zubehoerTypDTO) throws URISyntaxException {
        log.debug("REST request to update ZubehoerTyp : {}", zubehoerTypDTO);
        if (zubehoerTypDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ZubehoerTypDTO result = zubehoerTypService.save(zubehoerTypDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zubehoerTypDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /zubehoer-typs} : get all the zubehoerTyps.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of zubehoerTyps in body.
     */
    @GetMapping("/zubehoer-typs")
    public ResponseEntity<List<ZubehoerTypDTO>> getAllZubehoerTyps(ZubehoerTypCriteria criteria) {
        log.debug("REST request to get ZubehoerTyps by criteria: {}", criteria);
        List<ZubehoerTypDTO> entityList = zubehoerTypQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /zubehoer-typs/count} : count all the zubehoerTyps.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/zubehoer-typs/count")
    public ResponseEntity<Long> countZubehoerTyps(ZubehoerTypCriteria criteria) {
        log.debug("REST request to count ZubehoerTyps by criteria: {}", criteria);
        return ResponseEntity.ok().body(zubehoerTypQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /zubehoer-typs/:id} : get the "id" zubehoerTyp.
     *
     * @param id the id of the zubehoerTypDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zubehoerTypDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/zubehoer-typs/{id}")
    public ResponseEntity<ZubehoerTypDTO> getZubehoerTyp(@PathVariable Long id) {
        log.debug("REST request to get ZubehoerTyp : {}", id);
        Optional<ZubehoerTypDTO> zubehoerTypDTO = zubehoerTypService.findOne(id);
        return ResponseUtil.wrapOrNotFound(zubehoerTypDTO);
    }

    /**
     * {@code DELETE  /zubehoer-typs/:id} : delete the "id" zubehoerTyp.
     *
     * @param id the id of the zubehoerTypDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/zubehoer-typs/{id}")
    public ResponseEntity<Void> deleteZubehoerTyp(@PathVariable Long id) {
        log.debug("REST request to delete ZubehoerTyp : {}", id);
        zubehoerTypService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/zubehoer-typs?query=:query} : search for the zubehoerTyp corresponding
     * to the query.
     *
     * @param query the query of the zubehoerTyp search.
     * @return the result of the search.
     */
    @GetMapping("/_search/zubehoer-typs")
    public List<ZubehoerTypDTO> searchZubehoerTyps(@RequestParam String query) {
        log.debug("REST request to search ZubehoerTyps for query {}", query);
        return zubehoerTypService.search(query);
    }
}
