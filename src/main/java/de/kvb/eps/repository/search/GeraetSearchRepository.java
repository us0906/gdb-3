package de.kvb.eps.repository.search;
import de.kvb.eps.domain.Geraet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Geraet} entity.
 */
public interface GeraetSearchRepository extends ElasticsearchRepository<Geraet, Long> {
}
