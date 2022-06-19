package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorizationserver

import com.example.v1oauthauthorizationservice.infrastructure.configuration.cache.CacheNames
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.RedirectUriEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.RegisteredClientEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions.RegisteredClientNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.property.OAuthTokenProperties
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository.RedirectUriEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository.RegisteredClientEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.UuidUtils.toUUID
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.OAuth2TokenFormat
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient.Builder
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.TokenSettings
import org.springframework.stereotype.Repository
import java.time.Duration
import java.util.UUID

@Repository
class CustomRegisteredClientRepository(
    private val registeredClientEntityRepository: RegisteredClientEntityRepository,
    private val redirectUriEntityRepository: RedirectUriEntityRepository,
    private val passwordEncoder: PasswordEncoder,
    private val oAuthTokenProperties: OAuthTokenProperties
) : RegisteredClientRepository {
    override fun save(registeredClient: RegisteredClient) {
        val registeredClientEntityToSave = registeredClient.toEntity()
        val savedRegisteredClientEntity = registeredClientEntityRepository.save(registeredClientEntityToSave)

        val redirectUriEntitiesToSave = registeredClient.redirectUris
            .map { it.toRedirectUriEntity(savedRegisteredClientEntity) }
        redirectUriEntityRepository.saveAll(redirectUriEntitiesToSave)
    }

    private fun String.toRedirectUriEntity(registeredClientEntity: RegisteredClientEntity) =
        RedirectUriEntity(
            uri = this,
            registeredClient = registeredClientEntity
        )

    private fun RegisteredClient.toEntity() =
        RegisteredClientEntity(
            clientId = this.clientId,
            clientSecret = passwordEncoder.encode(this.clientSecret),
            clientName = this.clientName,
            SecurityContextHolder.getContext().authentication.name.toUUID()
        )

    @Cacheable(cacheNames = [CacheNames.REGISTERED_CLIENT_CACHE_NAME], key = "#id")
    override fun findById(id: String): RegisteredClient {
        val registeredClientEntity = registeredClientEntityRepository.findByIdOrNull(id.toUUID())
            ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.ID_NOT_FOUND_MESSAGE)
        return registeredClientEntity.toRegisteredClientAndAddTokenSettings()
    }

    @Cacheable(cacheNames = [CacheNames.REGISTERED_CLIENT_CACHE_NAME], key = "#clientId")
    override fun findByClientId(clientId: String): RegisteredClient {
        val registeredClientEntity = registeredClientEntityRepository.findByClientId(clientId)
            ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.ID_NOT_FOUND_MESSAGE)
        return registeredClientEntity.toRegisteredClientAndAddTokenSettings()
    }

    private fun RegisteredClientEntity.toRegisteredClientAndAddTokenSettings() =
        RegisteredClient
            .withId(this.id.toString())
            .clientId(this.clientId)
            .clientName(this.clientName)
            .clientSecret(this.clientSecret)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .tokenSettings(buildTokenSettingsWithStaticValue())
            .addAllRedirectUrisFromDbWithRegisteredClientId(this.id)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .scope(OidcScopes.OPENID)
            .build()

    private fun buildTokenSettingsWithStaticValue() =
        TokenSettings.builder()
            .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
            .accessTokenTimeToLive(Duration.ofHours(oAuthTokenProperties.accessTokenExpirationByHour))
            .reuseRefreshTokens(false)
            .refreshTokenTimeToLive(Duration.ofDays(oAuthTokenProperties.refreshTokenExpirationByDay))
            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS512)
            .build()

    private fun Builder.addAllRedirectUrisFromDbWithRegisteredClientId(registeredClientId: UUID): Builder {
        redirectUriEntityRepository.findAllByRegisteredClientId(registeredClientId)
            .forEach { this.redirectUri(it.uri) }
        return this
    }
}
