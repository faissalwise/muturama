package ma.sobexime.muturama.repository.search;

import ma.sobexime.muturama.domain.Agent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Agent entity.
 */
public interface AgentSearchRepository extends ElasticsearchRepository<Agent, Long> {
}
