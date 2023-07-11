package com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request

data class RegisterClientRequest(
    val clientId: String,
    val redirectUris: List<String>
)
