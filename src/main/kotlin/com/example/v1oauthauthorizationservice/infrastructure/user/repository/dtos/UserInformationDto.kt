package com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos

import java.util.UUID

data class UserInformationDto(
    val name: String,
    val id: UUID,
    val password: String
)
