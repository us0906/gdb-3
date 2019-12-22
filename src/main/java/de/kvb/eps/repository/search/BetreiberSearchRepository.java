package de.kvb.eps.repository.search;
import de.kvb.eps.domain.Betreiber;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Betreiber} entity.
 */
public interface BetreiberSearchRepository extends ElasticsearchRepository<Betreiber, Long> {
}
