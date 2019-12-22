package de.kvb.eps.web.rest;

import de.kvb.eps.service.ZubehoerService;
import de.kvb.eps.web.rest.errors.BadRequestAlertException;
import de.kvb.eps.service.dto.ZubehoerDTO;

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
 * REST controller for managing {@link de.kvb.eps.domain.Zubehoer}.
 */
@RestController
@RequestMapping("/api")
public class ZubehoerResource {

    private final Logger log = LoggerFactory.getLogger(ZubehoerResource.class);

    private static final String ENTITY_NAME = "zubehoer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ZubehoerService zubehoerService;

    public ZubehoerResource(ZubehoerService zubehoerService) {
        this.zubehoerService = zubehoerService;
    }

    /**
     * {@code POST  /zubehoers} : Create a new zubehoer.
     *
     * @param zubehoerDTO the zubehoerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new zubehoerDTO, or with status {@code 400 (Bad Request)} if the zubehoer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/zubehoers")
    public ResponseEntity<ZubehoerDTO> createZubehoer(@Valid @RequestBody ZubehoerDTO zubehoerDTO) throws URISyntaxException {
        log.debug("REST request to save Zubehoer : {}", zubehoerDTO);
        if (zubehoerDTO.getId() != null) {
            throw new BadRequestAlertException("A new zubehoer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ZubehoerDTO result = zubehoerService.save(zubehoerDTO);
        return ResponseEntity.created(new URI("/api/zubehoers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /zubehoers} : Updates an existing zubehoer.
     *
     * @param zubehoerDTO the zubehoerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated zubehoerDTO,
     * or with status {@code 400 (Bad Request)} if the zubehoerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the zubehoerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/zubehoers")
    public ResponseEntity<ZubehoerDTO> updateZubehoer(@Valid @RequestBody ZubehoerDTO zubehoerDTO) throws URISyntaxException {
        log.debug("REST request to update Zubehoer : {}", zubehoerDTO);
        if (zubehoerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ZubehoerDTO result = zubehoerService.save(zubehoerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, zubehoerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /zubehoers} : get all the zubehoers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of zubehoers in body.
     */
    @GetMapping("/zubehoers")
    public List<ZubehoerDTO> getAllZubehoers() {
        log.debug("REST request to get all Zubehoers");
        return zubehoerService.findAll();
    }

    /**
     * {@code GET  /zubehoers/:id} : get the "id" zubehoer.
     *
     * @param id the id of the zubehoerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the zubehoerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/zubehoers/{id}")
    public ResponseEntity<ZubehoerDTO> getZubehoer(@PathVariable Long id) {
        log.debug("REST request to get Zubehoer : {}", id);
        Optional<ZubehoerDTO> zubehoerDTO = zubehoerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(zubehoerDTO);
    }

    /**
     * {@code DELETE  /zubehoers/:id} : delete the "id" zubehoer.
     *
     * @param id the id of the zubehoerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/zubehoers/{id}")
    public ResponseEntity<Void> deleteZubehoer(@PathVariable Long id) {
        log.debug("REST request to delete Zubehoer : {}", id);
        zubehoerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/zubehoers?query=:query} : search for the zubehoer corresponding
     * to the query.
     *
     * @param query the query of the zubehoer search.
     * @return the result of the search.
     */
    @GetMapping("/_search/zubehoers")
    public List<ZubehoerDTO> searchZubehoers(@RequestParam String query) {
        log.debug("REST request to search Zubehoers for query {}", query);
        return zubehoerService.search(query);
    }
}
