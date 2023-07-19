package com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response

data class ClientsResponse(
    val clients: List<ClientResponse>
) {
    data class ClientResponse(
        val clientId: String,
        val redirectUris: List<String>
    )
}
