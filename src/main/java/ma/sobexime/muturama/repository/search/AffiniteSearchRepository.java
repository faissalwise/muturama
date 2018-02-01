package ma.sobexime.muturama.repository.search;

import ma.sobexime.muturama.domain.Affinite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Affinite entity.
 */
public interface AffiniteSearchRepository extends ElasticsearchRepository<Affinite, Long> {
}
