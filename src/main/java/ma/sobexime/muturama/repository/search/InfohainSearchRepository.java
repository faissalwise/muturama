package ma.sobexime.muturama.repository.search;

import ma.sobexime.muturama.domain.Infohain;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Infohain entity.
 */
public interface InfohainSearchRepository extends ElasticsearchRepository<Infohain, Long> {
}
