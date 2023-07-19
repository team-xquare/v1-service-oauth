package com.example.v1oauthauthorizationservice.infrastructure.oauth2.mapper

import com.example.v1oauthauthorizationservice.domain.oauth.domain.RegisteredClientDto
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.RegisteredClientEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.property.OAuthTokenProperties
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository.RedirectUriEntityRepository
import java.time.Duration
import java.util.UUID
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.stereotype.Component

@Component
class RegisteredClientMapper(
    private val oAuthTokenProperties: OAuthTokenProperties,
    private val redirectUriEntityRepository: RedirectUriEntityRepository
) {
    fun toClient(entity: RegisteredClientEntity) = toDto(entity).run { toClient(this) }

    fun toClient(dto: RegisteredClientDto): RegisteredClient = dto.run {
        RegisteredClient
            .withId(id.toString())
            .commonSetting()
            .clientId(clientId)
            .clientName(clientName)
            .clientSecret(clientSecret)
            .redirectUris { uris ->
                uris.clear()
                uris.addAll(getRedirectUrisByClientId(dto.id))
            }
            .build()
    }

    private fun RegisteredClient.Builder.commonSetting() =
        authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .tokenSettings(buildTokenSettingsWithStaticValue())
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .scope(OidcScopes.OPENID)

    private fun buildTokenSettingsWithStaticValue() =
        TokenSettings.builder()
            .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
            .accessTokenTimeToLive(Duration.ofHours(oAuthTokenProperties.accessTokenExpirationByHour))
            .reuseRefreshTokens(false)
            .refreshTokenTimeToLive(Duration.ofDays(oAuthTokenProperties.refreshTokenExpirationByDay))
            .idTokenSignatureAlgorithm(SignatureAlgorithm.RS512)
            .build()

    fun toDto(entity: RegisteredClientEntity) = entity.run {
        RegisteredClientDto(
            id = id,
            clientId = clientId,
            clientSecret = clientSecret,
            userId = userId,
            redirectUris = getRedirectUrisByClientId(id)
        )
    }

    private fun getRedirectUrisByClientId(id: UUID) =
        redirectUriEntityRepository.findAllByRegisteredClientId(id).map { it.uri }

    fun toEntity(dto: RegisteredClientDto) = dto.run {
        RegisteredClientEntity(
            id = id,
            clientId = clientId,
            clientSecret = clientSecret,
            clientName = clientName,
            userId = userId
        )
    }
}
