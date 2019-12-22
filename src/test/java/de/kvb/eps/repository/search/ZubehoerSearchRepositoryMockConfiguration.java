package de.kvb.eps.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ZubehoerSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ZubehoerSearchRepositoryMockConfiguration {

    @MockBean
    private ZubehoerSearchRepository mockZubehoerSearchRepository;

}
