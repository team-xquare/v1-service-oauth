package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "oauth.token")
class OAuthTokenProperties(
    val accessTokenExpirationByHour: Long,
    val refreshTokenExpirationByDay: Long
)
