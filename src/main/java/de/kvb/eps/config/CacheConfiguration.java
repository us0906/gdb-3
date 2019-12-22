package de.kvb.eps.config;

import java.time.Duration;

import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;

import org.hibernate.cache.jcache.ConfigSettings;
import io.github.jhipster.config.JHipsterProperties;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, de.kvb.eps.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, de.kvb.eps.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, de.kvb.eps.domain.User.class.getName());
            createCache(cm, de.kvb.eps.domain.Authority.class.getName());
            createCache(cm, de.kvb.eps.domain.User.class.getName() + ".authorities");
            createCache(cm, de.kvb.eps.domain.GeraetTyp.class.getName());
            createCache(cm, de.kvb.eps.domain.GeraetTyp.class.getName() + ".geraets");
            createCache(cm, de.kvb.eps.domain.Hersteller.class.getName());
            createCache(cm, de.kvb.eps.domain.Hersteller.class.getName() + ".geraets");
            createCache(cm, de.kvb.eps.domain.Hersteller.class.getName() + ".zubehoers");
            createCache(cm, de.kvb.eps.domain.ZubehoerTyp.class.getName());
            createCache(cm, de.kvb.eps.domain.ZubehoerTyp.class.getName() + ".zubehoers");
            createCache(cm, de.kvb.eps.domain.Geraet.class.getName());
            createCache(cm, de.kvb.eps.domain.Geraet.class.getName() + ".systemtyps");
            createCache(cm, de.kvb.eps.domain.Zubehoer.class.getName());
            createCache(cm, de.kvb.eps.domain.Zubehoer.class.getName() + ".systemtyps");
            createCache(cm, de.kvb.eps.domain.Systemtyp.class.getName());
            createCache(cm, de.kvb.eps.domain.Systemtyp.class.getName() + ".systeminstanzs");
            createCache(cm, de.kvb.eps.domain.Systeminstanz.class.getName());
            createCache(cm, de.kvb.eps.domain.Systeminstanz.class.getName() + ".systemnutzungs");
            createCache(cm, de.kvb.eps.domain.Arzt.class.getName());
            createCache(cm, de.kvb.eps.domain.Arzt.class.getName() + ".systemnutzungs");
            createCache(cm, de.kvb.eps.domain.Betreiber.class.getName());
            createCache(cm, de.kvb.eps.domain.Betreiber.class.getName() + ".systeminstanzs");
            createCache(cm, de.kvb.eps.domain.Betriebsstaette.class.getName());
            createCache(cm, de.kvb.eps.domain.Betriebsstaette.class.getName() + ".systeminstanzs");
            createCache(cm, de.kvb.eps.domain.Systemnutzung.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, jcacheConfiguration);
    }

}
