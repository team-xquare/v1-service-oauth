package com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response

import java.util.UUID

data class RegenerateSecretResponse(
    val clientSecret: String
)