package de.kvb.eps.repository.search;
import de.kvb.eps.domain.Zubehoer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Zubehoer} entity.
 */
public interface ZubehoerSearchRepository extends ElasticsearchRepository<Zubehoer, Long> {
}
