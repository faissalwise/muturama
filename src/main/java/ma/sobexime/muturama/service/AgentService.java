package ma.sobexime.muturama.service;

import ma.sobexime.muturama.domain.Agent;
import ma.sobexime.muturama.repository.AgentRepository;
import ma.sobexime.muturama.repository.search.AgentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Agent.
 */
@Service
@Transactional
public class AgentService {

    private final Logger log = LoggerFactory.getLogger(AgentService.class);

    private final AgentRepository agentRepository;

    private final AgentSearchRepository agentSearchRepository;

    public AgentService(AgentRepository agentRepository, AgentSearchRepository agentSearchRepository) {
        this.agentRepository = agentRepository;
        this.agentSearchRepository = agentSearchRepository;
    }

    /**
     * Save a agent.
     *
     * @param agent the entity to save
     * @return the persisted entity
     */
    public Agent save(Agent agent) {
        log.debug("Request to save Agent : {}", agent);
        Agent result = agentRepository.save(agent);
        agentSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the agents.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Agent> findAll(Pageable pageable) {
        log.debug("Request to get all Agents");
        return agentRepository.findAll(pageable);
    }

    /**
     *  Get one agent by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Agent findOne(Long id) {
        log.debug("Request to get Agent : {}", id);
        return agentRepository.findOne(id);
    }

    /**
     *  Delete the  agent by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Agent : {}", id);
        agentRepository.delete(id);
        agentSearchRepository.delete(id);
    }

    /**
     * Search for the agent corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Agent> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Agents for query {}", query);
        Page<Agent> result = agentSearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
