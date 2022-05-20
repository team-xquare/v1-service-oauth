package com.example.v1oauthauthorizationservice.domain.login.spi.dtos

import java.util.UUID
import java.io.Serializable

data class UserInformationSpiDto(
    val name: String,
    val id: UUID,
    val password: String
): Serializable
