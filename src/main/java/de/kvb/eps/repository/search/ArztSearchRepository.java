package de.kvb.eps.repository.search;

import de.kvb.eps.domain.Arzt;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Arzt} entity.
 */
public interface ArztSearchRepository extends ElasticsearchRepository<Arzt, Long> {
}
