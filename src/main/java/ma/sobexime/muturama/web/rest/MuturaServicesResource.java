package ma.sobexime.muturama.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.sobexime.muturama.domain.MuturaServices;

import ma.sobexime.muturama.repository.MuturaServicesRepository;
import ma.sobexime.muturama.repository.search.MuturaServicesSearchRepository;
import ma.sobexime.muturama.web.rest.errors.BadRequestAlertException;
import ma.sobexime.muturama.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing MuturaServices.
 */
@RestController
@RequestMapping("/api")
public class MuturaServicesResource {

    private final Logger log = LoggerFactory.getLogger(MuturaServicesResource.class);

    private static final String ENTITY_NAME = "muturaServices";

    private final MuturaServicesRepository muturaServicesRepository;

    private final MuturaServicesSearchRepository muturaServicesSearchRepository;

    public MuturaServicesResource(MuturaServicesRepository muturaServicesRepository, MuturaServicesSearchRepository muturaServicesSearchRepository) {
        this.muturaServicesRepository = muturaServicesRepository;
        this.muturaServicesSearchRepository = muturaServicesSearchRepository;
    }

    /**
     * POST  /mutura-services : Create a new muturaServices.
     *
     * @param muturaServices the muturaServices to create
     * @return the ResponseEntity with status 201 (Created) and with body the new muturaServices, or with status 400 (Bad Request) if the muturaServices has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/mutura-services")
    @Timed
    public ResponseEntity<MuturaServices> createMuturaServices(@RequestBody MuturaServices muturaServices) throws URISyntaxException {
        log.debug("REST request to save MuturaServices : {}", muturaServices);
        if (muturaServices.getId() != null) {
            throw new BadRequestAlertException("A new muturaServices cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MuturaServices result = muturaServicesRepository.save(muturaServices);
        muturaServicesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mutura-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mutura-services : Updates an existing muturaServices.
     *
     * @param muturaServices the muturaServices to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated muturaServices,
     * or with status 400 (Bad Request) if the muturaServices is not valid,
     * or with status 500 (Internal Server Error) if the muturaServices couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/mutura-services")
    @Timed
    public ResponseEntity<MuturaServices> updateMuturaServices(@RequestBody MuturaServices muturaServices) throws URISyntaxException {
        log.debug("REST request to update MuturaServices : {}", muturaServices);
        if (muturaServices.getId() == null) {
            return createMuturaServices(muturaServices);
        }
        MuturaServices result = muturaServicesRepository.save(muturaServices);
        muturaServicesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, muturaServices.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mutura-services : get all the muturaServices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of muturaServices in body
     */
    @GetMapping("/mutura-services")
    @Timed
    public List<MuturaServices> getAllMuturaServices() {
        log.debug("REST request to get all MuturaServices");
        return muturaServicesRepository.findAll();
        }

    /**
     * GET  /mutura-services/:id : get the "id" muturaServices.
     *
     * @param id the id of the muturaServices to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the muturaServices, or with status 404 (Not Found)
     */
    @GetMapping("/mutura-services/{id}")
    @Timed
    public ResponseEntity<MuturaServices> getMuturaServices(@PathVariable Long id) {
        log.debug("REST request to get MuturaServices : {}", id);
        MuturaServices muturaServices = muturaServicesRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(muturaServices));
    }

    /**
     * DELETE  /mutura-services/:id : delete the "id" muturaServices.
     *
     * @param id the id of the muturaServices to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/mutura-services/{id}")
    @Timed
    public ResponseEntity<Void> deleteMuturaServices(@PathVariable Long id) {
        log.debug("REST request to delete MuturaServices : {}", id);
        muturaServicesRepository.delete(id);
        muturaServicesSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/mutura-services?query=:query : search for the muturaServices corresponding
     * to the query.
     *
     * @param query the query of the muturaServices search
     * @return the result of the search
     */
    @GetMapping("/_search/mutura-services")
    @Timed
    public List<MuturaServices> searchMuturaServices(@RequestParam String query) {
        log.debug("REST request to search MuturaServices for query {}", query);
        return StreamSupport
            .stream(muturaServicesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
