package de.kvb.eps.repository.search;

import de.kvb.eps.domain.GeraetTyp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link GeraetTyp} entity.
 */
public interface GeraetTypSearchRepository extends ElasticsearchRepository<GeraetTyp, Long> {
}
