package de.kvb.eps.repository.search;

import de.kvb.eps.domain.Systeminstanz;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Systeminstanz} entity.
 */
public interface SysteminstanzSearchRepository extends ElasticsearchRepository<Systeminstanz, Long> {
}
