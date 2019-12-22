package de.kvb.eps.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SystemtypSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SystemtypSearchRepositoryMockConfiguration {

    @MockBean
    private SystemtypSearchRepository mockSystemtypSearchRepository;

}
