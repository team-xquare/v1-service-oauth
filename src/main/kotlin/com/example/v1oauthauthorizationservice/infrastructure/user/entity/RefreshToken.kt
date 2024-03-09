package com.example.v1oauthauthorizationservice.infrastructure.user.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(timeToLive = 60 * 24 * 7)
data class RefreshToken(

    @Id
    var token: String,

    @Indexed
    var accountId: String,
)