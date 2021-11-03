package br.com.acredita.applicationlog.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

// https://datamify.com/spring/distributed-cache-in-spring-boot-application-with-redis/#:~:text=The%20same%20logic%20is%20applied%20as%20for%20PostgreSQL.,outside%20docker%20network%20localhost%3A6380%20has%20to%20be%20used.
// https://www.baeldung.com/spring-boot-redis-cache
@Configuration
@EnableCaching
public class CacheConfiguration {

    public static final String LOG = "log";

    // Aparentemente é mais performatico cache em binario 
    // @Bean
    // public RedisCacheConfiguration redisCacheConfiguration() {
    //     return RedisCacheConfiguration
    //             .defaultCacheConfig()
    //             .entryTtl(Duration.ofMinutes(60))
    //             .disableCachingNullValues()
    //             .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    // }
    

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        RedisCacheConfiguration configuration = RedisCacheConfiguration
            .defaultCacheConfig();
            // Aparentemente é mais performatico cache em binario 
            //.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return (builder) -> builder
            .withCacheConfiguration(LOG,configuration.entryTtl(Duration.ofSeconds(30)))
            .enableStatistics();
        
    }

}