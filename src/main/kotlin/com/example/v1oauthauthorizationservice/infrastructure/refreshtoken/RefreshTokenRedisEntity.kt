package com.example.v1oauthauthorizationservice.infrastructure.refreshtoken

import java.util.UUID
import java.util.concurrent.TimeUnit
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash(value = "refreshToken")
class RefreshTokenRedisEntity(
    @Id
    val tokenValue: String,

    val userId: UUID,

    @TimeToLive(unit = TimeUnit.HOURS)
    val timeToLive: Long
)
