package com.example.v1oauthauthorizationservice.global.env.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
data class TokenProperty(
    val secretKey: String,
    val accessExp: Long,
    val refreshExp: Long
)