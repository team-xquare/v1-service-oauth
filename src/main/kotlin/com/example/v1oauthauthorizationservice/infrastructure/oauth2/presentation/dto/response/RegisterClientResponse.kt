package com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response

data class RegisterClientResponse(
    val clientId: String,
    val clientSecret: String,
    val redirectUris: List<String>
)
