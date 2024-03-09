package com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response

import java.time.LocalDateTime

data class TokenResponse(
    val accessToken: String,
    val accessExpiredAt: LocalDateTime,
    val refreshToken: String,
    val refreshExpiredAt: LocalDateTime
)