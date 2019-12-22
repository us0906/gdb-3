package de.kvb.eps.repository.search;
import de.kvb.eps.domain.Hersteller;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Hersteller} entity.
 */
public interface HerstellerSearchRepository extends ElasticsearchRepository<Hersteller, Long> {
}
