package ma.sobexime.muturama.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.sobexime.muturama.domain.Infohain;

import ma.sobexime.muturama.repository.InfohainRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Infohain.
 */
@RestController
@RequestMapping("/api")
public class InfohainResource {

    private final Logger log = LoggerFactory.getLogger(InfohainResource.class);

    private static final String ENTITY_NAME = "infohain";

    private final InfohainRepository infohainRepository;

    public InfohainResource(InfohainRepository infohainRepository) {
        this.infohainRepository = infohainRepository;
    }

    /**
     * POST  /infohains : Create a new infohain.
     *
     * @param infohain the infohain to create
     * @return the ResponseEntity with status 201 (Created) and with body the new infohain, or with status 400 (Bad Request) if the infohain has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/infohains")
    @Timed
    public ResponseEntity<Infohain> createInfohain(@Valid @RequestBody Infohain infohain) throws URISyntaxException {
        log.debug("REST request to save Infohain : {}", infohain);
        if (infohain.getId() != null) {
            throw new BadRequestAlertException("A new infohain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Infohain result = infohainRepository.save(infohain);
        return ResponseEntity.created(new URI("/api/infohains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /infohains : Updates an existing infohain.
     *
     * @param infohain the infohain to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated infohain,
     * or with status 400 (Bad Request) if the infohain is not valid,
     * or with status 500 (Internal Server Error) if the infohain couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/infohains")
    @Timed
    public ResponseEntity<Infohain> updateInfohain(@Valid @RequestBody Infohain infohain) throws URISyntaxException {
        log.debug("REST request to update Infohain : {}", infohain);
        if (infohain.getId() == null) {
            return createInfohain(infohain);
        }
        Infohain result = infohainRepository.save(infohain);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, infohain.getId().toString()))
            .body(result);
    }

    /**
     * GET  /infohains : get all the infohains.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of infohains in body
     */
    @GetMapping("/infohains")
    @Timed
    public ResponseEntity<List<Infohain>> getAllInfohains(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Infohains");
        Page<Infohain> page = infohainRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/infohains");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /infohains/:id : get the "id" infohain.
     *
     * @param id the id of the infohain to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the infohain, or with status 404 (Not Found)
     */
    @GetMapping("/infohains/{id}")
    @Timed
    public ResponseEntity<Infohain> getInfohain(@PathVariable Long id) {
        log.debug("REST request to get Infohain : {}", id);
        Infohain infohain = infohainRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(infohain));
    }

    /**
     * DELETE  /infohains/:id : delete the "id" infohain.
     *
     * @param id the id of the infohain to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/infohains/{id}")
    @Timed
    public ResponseEntity<Void> deleteInfohain(@PathVariable Long id) {
        log.debug("REST request to delete Infohain : {}", id);
        infohainRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
