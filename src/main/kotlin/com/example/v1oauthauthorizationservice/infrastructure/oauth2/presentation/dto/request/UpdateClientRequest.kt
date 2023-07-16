package com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request

data class UpdateClientRequest(
    val redirectUris: List<String>
)
