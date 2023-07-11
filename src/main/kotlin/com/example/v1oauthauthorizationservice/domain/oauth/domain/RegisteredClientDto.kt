package com.example.v1oauthauthorizationservice.domain.oauth.domain

import java.util.UUID

data class RegisteredClientDto(
    val id: UUID = UUID(0, 0),
    val clientId: String,
    val clientSecret: String,
    val userId: UUID,
    val redirectUris: List<String> = mutableListOf()
) {
    val clientName: String = "$clientId-$userId"

    fun updateInfo(
        redirectUris: List<String> = this.redirectUris,
        clientSecret: String = this.clientSecret
    ) = copy(
        redirectUris = redirectUris,
        clientSecret = clientSecret
    )

    companion object {
        fun generateClientSecret() = UUID.randomUUID().toString()
    }
}
