package com.example.v1oauthauthorizationservice.domain.login.spi.dtos

import java.io.Serializable
import java.util.UUID

data class UserInformationSpiDto(
    val name: String,
    val id: UUID,
    val password: String
) : Serializable
