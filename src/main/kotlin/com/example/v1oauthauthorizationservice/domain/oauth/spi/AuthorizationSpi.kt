package com.example.v1oauthauthorizationservice.domain.oauth.spi

import java.util.UUID

interface AuthorizationSpi {
    fun save(clientId: String, userId: UUID, authorizationGrantType: String, authorizedScopes: Set<String>)
}