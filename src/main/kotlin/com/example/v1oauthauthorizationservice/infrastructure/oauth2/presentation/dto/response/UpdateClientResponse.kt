package com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response

data class UpdateClientResponse(
    val clientId: String,
    val redirectUris: List<String>
)