package ma.sobexime.muturama.web.rest;

import com.codahale.metrics.annotation.Timed;
import ma.sobexime.muturama.domain.AgentList;

import ma.sobexime.muturama.repository.AgentListRepository;
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
 * REST controller for managing AgentList.
 */
@RestController
@RequestMapping("/api")
public class AgentListResource {

    private final Logger log = LoggerFactory.getLogger(AgentListResource.class);

    private static final String ENTITY_NAME = "agentList";

    private final AgentListRepository agentListRepository;

    public AgentListResource(AgentListRepository agentListRepository) {
        this.agentListRepository = agentListRepository;
    }

    /**
     * POST  /agent-lists : Create a new agentList.
     *
     * @param agentList the agentList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agentList, or with status 400 (Bad Request) if the agentList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agent-lists")
    @Timed
    public ResponseEntity<AgentList> createAgentList(@RequestBody AgentList agentList) throws URISyntaxException {
        log.debug("REST request to save AgentList : {}", agentList);
        if (agentList.getId() != null) {
            throw new BadRequestAlertException("A new agentList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentList result = agentListRepository.save(agentList);
        return ResponseEntity.created(new URI("/api/agent-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agent-lists : Updates an existing agentList.
     *
     * @param agentList the agentList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agentList,
     * or with status 400 (Bad Request) if the agentList is not valid,
     * or with status 500 (Internal Server Error) if the agentList couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agent-lists")
    @Timed
    public ResponseEntity<AgentList> updateAgentList(@RequestBody AgentList agentList) throws URISyntaxException {
        log.debug("REST request to update AgentList : {}", agentList);
        if (agentList.getId() == null) {
            return createAgentList(agentList);
        }
        AgentList result = agentListRepository.save(agentList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agentList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agent-lists : get all the agentLists.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agentLists in body
     */
    @GetMapping("/agent-lists")
    @Timed
    public ResponseEntity<List<AgentList>> getAllAgentLists(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of AgentLists");
        Page<AgentList> page = agentListRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agent-lists");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agent-lists/:id : get the "id" agentList.
     *
     * @param id the id of the agentList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agentList, or with status 404 (Not Found)
     */
    @GetMapping("/agent-lists/{id}")
    @Timed
    public ResponseEntity<AgentList> getAgentList(@PathVariable Long id) {
        log.debug("REST request to get AgentList : {}", id);
        AgentList agentList = agentListRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(agentList));
    }

    /**
     * DELETE  /agent-lists/:id : delete the "id" agentList.
     *
     * @param id the id of the agentList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agent-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgentList(@PathVariable Long id) {
        log.debug("REST request to delete AgentList : {}", id);
        agentListRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
