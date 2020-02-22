package de.kvb.eps.repository.search;

import de.kvb.eps.domain.Systemtyp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Systemtyp} entity.
 */
public interface SystemtypSearchRepository extends ElasticsearchRepository<Systemtyp, Long> {
}
