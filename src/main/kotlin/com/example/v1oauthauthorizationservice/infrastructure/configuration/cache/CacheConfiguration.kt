package com.example.v1oauthauthorizationservice.infrastructure.configuration.cache

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfiguration {
    @Bean
    @Override
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val configuration = RedisCacheConfiguration
            .defaultCacheConfig(Thread.currentThread().contextClassLoader)
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    JdkSerializationRedisSerializer()
                )
            )
            .prefixCacheNameWith("cache-")
            .entryTtl(Duration.ofMinutes(3))

        val builder = RedisCacheManager
            .RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(configuration)

        return builder.build()
    }
}
