package de.kvb.eps.repository.search;
import de.kvb.eps.domain.ZubehoerTyp;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ZubehoerTyp} entity.
 */
public interface ZubehoerTypSearchRepository extends ElasticsearchRepository<ZubehoerTyp, Long> {
}
