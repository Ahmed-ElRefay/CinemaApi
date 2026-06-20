package com.example.cinema.common.config

import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import tools.jackson.databind.json.JsonMapper
import java.time.Duration



@Configuration
class CacheConfig {
    @Bean
    fun cacheCustomizer(jsonMapper: JsonMapper): RedisCacheManagerBuilderCustomizer {
       val jsonSerializer = GenericJacksonJsonRedisSerializer
           .builder()
           .enableUnsafeDefaultTyping()
           .build()

        return RedisCacheManagerBuilderCustomizer { builder ->
            builder
                .withCacheConfiguration(
                    "showtime-seats",
                    RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofSeconds(30))
                        .serializeValuesWith(
                            RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
                        ),
                )
        }
    }
}