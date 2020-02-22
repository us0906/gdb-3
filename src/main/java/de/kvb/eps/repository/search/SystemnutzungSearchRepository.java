package de.kvb.eps.repository.search;

import de.kvb.eps.domain.Systemnutzung;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Systemnutzung} entity.
 */
public interface SystemnutzungSearchRepository extends ElasticsearchRepository<Systemnutzung, Long> {
}
