package com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request

import jakarta.validation.constraints.NotNull

data class ReissueRequest(
    @field:NotNull
    val refreshToken: String
)