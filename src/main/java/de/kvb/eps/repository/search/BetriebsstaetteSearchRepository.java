package de.kvb.eps.repository.search;
import de.kvb.eps.domain.Betriebsstaette;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Betriebsstaette} entity.
 */
public interface BetriebsstaetteSearchRepository extends ElasticsearchRepository<Betriebsstaette, Long> {
}
