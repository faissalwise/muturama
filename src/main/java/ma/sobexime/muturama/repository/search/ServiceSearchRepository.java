package ma.sobexime.muturama.repository.search;

import ma.sobexime.muturama.domain.Service;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Service entity.
 */
public interface ServiceSearchRepository extends ElasticsearchRepository<Service, Long> {
}
