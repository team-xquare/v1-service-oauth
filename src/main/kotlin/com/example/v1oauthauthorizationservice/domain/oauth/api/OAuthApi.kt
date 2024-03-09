package com.example.v1oauthauthorizationservice.domain.oauth.api

import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request.RegisterClientRequest
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request.UpdateClientRequest
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.ClientsResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.RegenerateSecretResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.RegisterClientResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.UpdateClientResponse

interface OAuthApi {
    fun getClient(): ClientsResponse
    fun registerClient(request: RegisterClientRequest): RegisterClientResponse
    fun updateClient(clientId: String, request: UpdateClientRequest): UpdateClientResponse
    fun regenerateSecret(clientId: String): RegenerateSecretResponse
}
