package ma.sobexime.muturama.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(ma.sobexime.muturama.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.Service.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.Service.class.getName() + ".user_services", jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.Agent.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.AgentList.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.City.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.Affinite.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.MuturaServices.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.Job.class.getName(), jcacheConfiguration);
            cm.createCache(ma.sobexime.muturama.domain.Infohain.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
