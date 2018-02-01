package ma.sobexime.muturama.repository.search;

import ma.sobexime.muturama.domain.MuturaServices;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the MuturaServices entity.
 */
public interface MuturaServicesSearchRepository extends ElasticsearchRepository<MuturaServices, Long> {
}
