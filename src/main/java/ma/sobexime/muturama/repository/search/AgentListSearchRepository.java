package ma.sobexime.muturama.repository.search;

import ma.sobexime.muturama.domain.AgentList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AgentList entity.
 */
public interface AgentListSearchRepository extends ElasticsearchRepository<AgentList, Long> {
}
