package com.example.v1oauthauthorizationservice.domain.oauth.api

import com.example.v1oauthauthorizationservice.domain.oauth.domain.RegisteredClientDto
import com.example.v1oauthauthorizationservice.domain.oauth.spi.RegisteredClientSpi
import com.example.v1oauthauthorizationservice.domain.security.spi.SecuritySpi
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions.RegisteredClientAlreadyExistsException
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request.RegisterClientRequest
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request.UpdateClientRequest
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.RegisterClientResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.ClientsResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.RegenerateSecretResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.UpdateClientResponse
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class OAuthApiImpl(
    private val registeredClientSpi: RegisteredClientSpi,
    private val securitySpi: SecuritySpi
) : OAuthApi {

    override fun getClient(request: RegisterClientRequest): ClientsResponse {
        val userId = securitySpi.getCurrentUserId()
        return ClientsResponse(
            registeredClientSpi.getByUserId(userId)
                .map {
                    ClientsResponse.ClientResponse(
                        clientId = it.clientId,
                        redirectUris = it.redirectUris
                    )
                }
        )
    }

    override fun registerClient(request: RegisterClientRequest): RegisterClientResponse {

        if (registeredClientSpi.existsByClientId(request.clientId)) {
            throw RegisteredClientAlreadyExistsException(RegisteredClientAlreadyExistsException.CLIENT_ID_ALREADY_EXISTS_MESSAGE)
        }

        val clientSecret = RegisteredClientDto.generateClientSecret()
        registeredClientSpi.save(
            RegisteredClientDto(
                userId = securitySpi.getCurrentUserId(),
                clientId = request.clientId,
                clientSecret = securitySpi.encode(clientSecret),
                redirectUris = request.redirectUris
            )
        )

        return RegisterClientResponse(
            clientId = request.clientId,
            clientSecret = clientSecret,
            redirectUris = request.redirectUris
        )
    }

    override fun updateClient(clientId: String, request: UpdateClientRequest): UpdateClientResponse {
        val client = registeredClientSpi.findByClientId(clientId)

        val updatedClient = client.updateInfo(
            redirectUris = request.redirectUris
        ).also {
            registeredClientSpi.save(it)
        }

        return UpdateClientResponse(
            clientId = updatedClient.clientId,
            redirectUris = updatedClient.redirectUris.toList()
        )
    }

    override fun regenerateSecret(clientId: String): RegenerateSecretResponse {
        val clientSecret = RegisteredClientDto.generateClientSecret()
        val client = registeredClientSpi.findByClientId(clientId)

        client.updateInfo(
            clientSecret = securitySpi.encode(clientSecret)
        ).also {
            registeredClientSpi.save(it)
        }

        return RegenerateSecretResponse(
            clientSecret = clientSecret
        )
    }
}
