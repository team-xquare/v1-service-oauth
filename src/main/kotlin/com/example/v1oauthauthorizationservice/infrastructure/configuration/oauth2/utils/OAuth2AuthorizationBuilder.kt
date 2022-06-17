package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.RegisteredClientEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AccessTokenEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationAttributeEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationCodeEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.OidcIdTokenEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.RefreshTokenEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.UuidUtils.toUUID
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.UUID
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient

object OAuth2AuthorizationBuilder {
    fun AuthorizationEntity.toOAuth2Authorization(
        registeredClient: RegisteredClient,
        objectMapper: ObjectMapper
    ): OAuth2Authorization {
        val builder = OAuth2Authorization.withRegisteredClient(registeredClient)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .principalName(this.principalName)
            .id(this.id.toString())

        val authorizationCodeEntityOrNull = this.authorizationCodeEntity
        authorizationCodeEntityOrNull?.let {
            builder.token(it.toOAuth2AuthorizationCode())

            if (it.state != null) {
                builder.attribute(OAuth2ParameterNames.STATE, it.state)
            }
        }

        val accessTokenEntityOrNull = this.accessTokenEntity
        accessTokenEntityOrNull?.let {
            builder.accessToken(it.toOAuth2AccessToken())
        }

        val refreshTokenEntityOrNull = this.refreshTokenEntity
        refreshTokenEntityOrNull?.let {
            builder.refreshToken(it.toOAuth2RefreshToken())
        }

        val oidcIdTokenEntityOrNull = this.oidcIdTokenEntity
        oidcIdTokenEntityOrNull?.let {
            builder.token(
                OidcIdToken(
                    it.tokenValue,
                    it.issuedAt,
                    it.expiredAt,
                    mutableMapOf("test" to "test") as Map<String, Any>?
                )
            )
        }

        val authorizationAttributeEntities = this.authorizationAttributeEntities
        val authorizationAttributeJsonNode =
            authorizationAttributeEntities.toAuthorizationAttributeJsonNode(objectMapper)

        builder.attributes { t ->
            t.putAll(
                objectMapper.readValue(
                    authorizationAttributeJsonNode.toString(),
                    object : TypeReference<Map<String, Any>>() {}
                )
            )
        }

        return builder.build()
    }

    private fun AuthorizationCodeEntity.toOAuth2AuthorizationCode() =
        OAuth2AuthorizationCode(
            this.codeValue,
            this.issuedAt,
            this.expiredAt
        )

    private fun AccessTokenEntity.toOAuth2AccessToken() =
        OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            this.tokenValue,
            this.issuedAt,
            this.expiredAt
        )

    private fun RefreshTokenEntity.toOAuth2RefreshToken() =
        OAuth2RefreshToken(
            this.tokenValue,
            this.issuedAt,
            this.expiredAt,
        )

    private fun List<AuthorizationAttributeEntity>.toAuthorizationAttributeJsonNode(objectMapper: ObjectMapper): JsonNode {
        val authorizationAttributes = objectMapper.createObjectNode()
        this.forEach {
            authorizationAttributes.set<JsonNode>(it.attributeKey, it.attributeValue)
        }
        return authorizationAttributes.put("@class", "java.util.Collections\$UnmodifiableMap")
    }

    fun buildAccessTokenEntity(
        authorizationEntity: AuthorizationEntity,
        oAuth2AccessToken: OAuth2AccessToken
    ) = AccessTokenEntity(
        authorization = authorizationEntity,
        expiredAt = oAuth2AccessToken.expiresAt!!,
        tokenValue = oAuth2AccessToken.tokenValue
    )

    fun buildRefreshTokenEntity(
        authorizationEntity: AuthorizationEntity,
        oAuth2RefreshToken: OAuth2RefreshToken
    ) = RefreshTokenEntity(
        authorization = authorizationEntity,
        expiredAt = oAuth2RefreshToken.expiresAt!!,
        tokenValue = oAuth2RefreshToken.tokenValue
    )

    fun buildOidcTokenEntity(authorizationEntity: AuthorizationEntity, oidcIdToken: OidcIdToken) =
        OidcIdTokenEntity(
            authorization = authorizationEntity,
            tokenValue = oidcIdToken.tokenValue,
            expiredAt = oidcIdToken.expiresAt!!
        )

    fun buildAuthorizationEntity(
        oAuth2Authorization: OAuth2Authorization,
        registeredClientEntity: RegisteredClientEntity,
        userId: UUID
    ) = AuthorizationEntity(
        registeredClient = registeredClientEntity,
        authorizationGrantType = oAuth2Authorization.authorizationGrantType.value,
        principalName = oAuth2Authorization.principalName,
        id = oAuth2Authorization.id.toUUID(),
        userId = userId
    )

    fun buildAuthorizationCodeEntity(
        oAuth2AuthorizationCode: OAuth2AuthorizationCode,
        authorizationEntity: AuthorizationEntity,
        state: String?
    ) = AuthorizationCodeEntity(
        authorization = authorizationEntity,
        expiredAt = oAuth2AuthorizationCode.expiresAt!!,
        codeValue = oAuth2AuthorizationCode.tokenValue,
        state = state
    )
}
