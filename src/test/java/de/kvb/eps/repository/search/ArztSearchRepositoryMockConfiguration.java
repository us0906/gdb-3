package de.kvb.eps.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ArztSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ArztSearchRepositoryMockConfiguration {

    @MockBean
    private ArztSearchRepository mockArztSearchRepository;

}
