package ma.sobexime.muturama.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.sobexime.muturama.domain.Affinite;

import ma.sobexime.muturama.repository.AffiniteRepository;
import ma.sobexime.muturama.web.rest.errors.BadRequestAlertException;
import ma.sobexime.muturama.web.rest.util.HeaderUtil;
import ma.sobexime.muturama.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Affinite.
 */
@RestController
@RequestMapping("/api")
public class AffiniteResource {

    private final Logger log = LoggerFactory.getLogger(AffiniteResource.class);

    private static final String ENTITY_NAME = "affinite";

    private final AffiniteRepository affiniteRepository;

    public AffiniteResource(AffiniteRepository affiniteRepository) {
        this.affiniteRepository = affiniteRepository;
    }

    /**
     * POST  /affinites : Create a new affinite.
     *
     * @param affinite the affinite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new affinite, or with status 400 (Bad Request) if the affinite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/affinites")
    @Timed
    public ResponseEntity<Affinite> createAffinite(@RequestBody Affinite affinite) throws URISyntaxException {
        log.debug("REST request to save Affinite : {}", affinite);
        if (affinite.getId() != null) {
            throw new BadRequestAlertException("A new affinite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Affinite result = affiniteRepository.save(affinite);
        return ResponseEntity.created(new URI("/api/affinites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /affinites : Updates an existing affinite.
     *
     * @param affinite the affinite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated affinite,
     * or with status 400 (Bad Request) if the affinite is not valid,
     * or with status 500 (Internal Server Error) if the affinite couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/affinites")
    @Timed
    public ResponseEntity<Affinite> updateAffinite(@RequestBody Affinite affinite) throws URISyntaxException {
        log.debug("REST request to update Affinite : {}", affinite);
        if (affinite.getId() == null) {
            return createAffinite(affinite);
        }
        Affinite result = affiniteRepository.save(affinite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, affinite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /affinites : get all the affinites.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of affinites in body
     */
    @GetMapping("/affinites")
    @Timed
    public ResponseEntity<List<Affinite>> getAllAffinites(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Affinites");
        Page<Affinite> page = affiniteRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/affinites");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /affinites/:id : get the "id" affinite.
     *
     * @param id the id of the affinite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the affinite, or with status 404 (Not Found)
     */
    @GetMapping("/affinites/{id}")
    @Timed
    public ResponseEntity<Affinite> getAffinite(@PathVariable Long id) {
        log.debug("REST request to get Affinite : {}", id);
        Affinite affinite = affiniteRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(affinite));
    }

    /**
     * DELETE  /affinites/:id : delete the "id" affinite.
     *
     * @param id the id of the affinite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/affinites/{id}")
    @Timed
    public ResponseEntity<Void> deleteAffinite(@PathVariable Long id) {
        log.debug("REST request to delete Affinite : {}", id);
        affiniteRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
