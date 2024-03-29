package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorizationserver

import com.example.v1oauthauthorizationservice.infrastructure.configuration.cache.CacheNames
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AccessTokenEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationAttributeEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions.AccessTokenNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions.AuthorizationNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions.RefreshTokenNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.AccessTokenEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.AuthorizationAttributeEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.AuthorizationCodeEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.AuthorizationEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.OidcIdTokenEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.RefreshTokenEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions.RegisteredClientNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository.RegisteredClientEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils.OAuth2AuthorizationBuilder
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils.OAuth2AuthorizationBuilder.buildAccessTokenEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils.OAuth2AuthorizationBuilder.buildAuthorizationCodeEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils.OAuth2AuthorizationBuilder.buildAuthorizationEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils.OAuth2AuthorizationBuilder.buildRefreshTokenEntity
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils.OAuth2AuthorizationBuilder.toOAuth2Authorization
import com.example.v1oauthauthorizationservice.infrastructure.configuration.objectmapper.ObjectMapperConfiguration
import com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.UuidUtils.toUUID
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.Resource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ACCESS_TOKEN
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CODE
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.STATE
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Transactional
@Component
class CustomOAuth2AuthorizationService(
    private val accessTokenEntityRepository: AccessTokenEntityRepository,
    private val authorizationCodeEntityRepository: AuthorizationCodeEntityRepository,
    private val oidcIdTokenEntityRepository: OidcIdTokenEntityRepository,
    private val refreshTokenEntityRepository: RefreshTokenEntityRepository,
    private val authorizationEntityRepository: AuthorizationEntityRepository,
    private val registeredClientRepository: RegisteredClientRepository,
    private val registeredClientEntityRepository: RegisteredClientEntityRepository,
    private val authorizationAttributeEntityRepository: AuthorizationAttributeEntityRepository,
    @Resource(name = ObjectMapperConfiguration.AUTHORIZATION_SERVER_BEAN_NAME)
    private val objectMapper: ObjectMapper
) : OAuth2AuthorizationService {

    override fun save(authorization: OAuth2Authorization) {
        val authorizationEntityId = authorization.id.toUUID()
        val authorizationEntity = authorizationEntityRepository.findByIdOrNull(authorizationEntityId)
        if (authorizationEntity == null) {
            saveAuthorizationAndAuthorizationCode(authorization)
        } else {
            saveTokensIfPresent(authorization, authorizationEntity)
        }
    }
    
    private fun saveAuthorizationAndAuthorizationCode(
        oAuth2Authorization: OAuth2Authorization
    ) {
        val registeredClientId = oAuth2Authorization.registeredClientId.toUUID()

        val registeredClientEntity = registeredClientEntityRepository.findByIdOrNull(registeredClientId)
            ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.ID_NOT_FOUND_MESSAGE)

        val authenticationName = SecurityContextHolder.getContext().authentication.name

        val userId = authenticationName.toUUID()

        val authorizationEntityToSave = buildAuthorizationEntity(oAuth2Authorization, registeredClientEntity, userId)
        val savedAuthorizationEntity = authorizationEntityRepository.save(authorizationEntityToSave)

        val authorizationRequest =
            oAuth2Authorization.getAttribute<OAuth2AuthorizationRequest>(OAuth2AuthorizationRequest::class.java.name)
        val state = authorizationRequest?.state

        val authorizationCode = oAuth2Authorization.getToken(OAuth2AuthorizationCode::class.java)!!.token
        val authorizationCodeEntity = buildAuthorizationCodeEntity(authorizationCode, savedAuthorizationEntity, state)
        authorizationCodeEntityRepository.save(authorizationCodeEntity)

        val authorizationAttributeEntities = oAuth2Authorization.attributes.map { entry ->
            AuthorizationAttributeEntity(
                attributeKey = entry.key,
                attributeValue = objectMapper.valueToTree(entry.value),
                authorization = savedAuthorizationEntity
            )
        }
        authorizationAttributeEntityRepository.saveAll(authorizationAttributeEntities)
    }

    private fun saveTokensIfPresent(
        oAuth2Authorization: OAuth2Authorization,
        authorizationEntity: AuthorizationEntity
    ) {
        val accessTokenEntityFromDb = accessTokenEntityRepository.findByIdOrNull(authorizationEntity.id)

        // 토큰이 사용되었는지에 대한 기준이 Access Token Entity

        if (accessTokenEntityFromDb == null) {
            saveNewTokens(
                oAuth2Authorization = oAuth2Authorization,
                authorizationEntity = authorizationEntity
            )
        } else {
            updateTokens(
                oAuth2Authorization = oAuth2Authorization,
                authorizationEntity = authorizationEntity,
                accessTokenEntity = accessTokenEntityFromDb
            )
        }
    }

    private fun saveNewTokens(
        oAuth2Authorization: OAuth2Authorization,
        authorizationEntity: AuthorizationEntity
    ) {
        accessTokenEntityRepository.save(
            buildAccessTokenEntity(
                authorizationEntity,
                oAuth2Authorization.accessToken.token
            )
        )

        oAuth2Authorization.refreshToken?.token?.let {
            val refreshTokenEntity = buildRefreshTokenEntity(authorizationEntity, it)
            refreshTokenEntityRepository.save(refreshTokenEntity)
        }

        oAuth2Authorization.getToken(OidcIdToken::class.java)?.token?.let {
            val oidcTokenEntity = OAuth2AuthorizationBuilder.buildOidcTokenEntity(authorizationEntity, it)
            oidcIdTokenEntityRepository.save(oidcTokenEntity)
        }

        authorizationCodeEntityRepository.deleteById(authorizationEntity.id)
    }

    private fun updateTokens(
        oAuth2Authorization: OAuth2Authorization,
        accessTokenEntity: AccessTokenEntity,
        authorizationEntity: AuthorizationEntity
    ) {
        val accessToken = oAuth2Authorization.accessToken.token
        accessTokenEntity.updateTokenValue(accessToken.tokenValue)
        accessTokenEntity.updateExpiration(accessToken.issuedAt!!, accessToken.expiresAt!!)

        val refreshToken = oAuth2Authorization.refreshToken?.token!!
        authorizationEntity.refreshTokenEntity!!.apply {
            updateTokenValue(refreshToken.tokenValue)
            updateExpiration(refreshToken.issuedAt!!, refreshToken.expiresAt!!)
        }

        val oidcToken = oAuth2Authorization.getToken(OidcIdToken::class.java)?.token
        oidcToken?.let {
            authorizationEntity.oidcIdTokenEntity?.apply {
                updateTokenValue(it.tokenValue)
                updateExpiration(it.issuedAt!!, it.expiresAt!!)
            }
        }
    }

    override fun remove(authorization: OAuth2Authorization) {
        val authorizationEntityId = authorization.id.toUUID()
        authorizationEntityRepository.deleteById(authorizationEntityId)
    }

    override fun findById(id: String): OAuth2Authorization {
        val authorizationEntityId = id.toUUID()

        val authorizationEntity = authorizationEntityRepository.findByIdOrNull(authorizationEntityId)
            ?: throw AuthorizationNotFoundException(AuthorizationNotFoundException.AUTHORIZATION_ID_NOT_FOUND)

        val registeredClient =
            registeredClientRepository.findByClientId(authorizationEntity.registeredClient.clientId)
                ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.ID_NOT_FOUND_MESSAGE)

        return authorizationEntity.toOAuth2Authorization(registeredClient, objectMapper)
    }

    @Cacheable(cacheNames = [CacheNames.AUTHORIZATION_CACHE_NAME], key = "#token")
    override fun findByToken(token: String, tokenType: OAuth2TokenType?): OAuth2Authorization {
        val authorizationEntity = getAuthorizationEntityByTokenType(token, tokenType)
        val registeredClient = registeredClientRepository.findByClientId(authorizationEntity.registeredClient.clientId)
            ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.ID_NOT_FOUND_MESSAGE)

        return authorizationEntity.toOAuth2Authorization(registeredClient, objectMapper)
    }

    private fun getAuthorizationEntityByTokenType(token: String, tokenType: OAuth2TokenType?): AuthorizationEntity {
        return when (tokenType?.value) {
            STATE -> authorizationCodeEntityRepository.findByState(token)?.authorization
                ?: throw AuthorizationNotFoundException(AuthorizationNotFoundException.AUTHORIZATION_STATE_NOT_FOUND)
            CODE -> authorizationCodeEntityRepository.findByCodeValue(token)?.authorization
                ?: throw AuthorizationNotFoundException(AuthorizationNotFoundException.AUTHORIZATION_CODE_NOT_FOUND)
            ACCESS_TOKEN -> accessTokenEntityRepository.findByTokenValue(token)?.authorization
                ?: throw AccessTokenNotFoundException(AccessTokenNotFoundException.ACCESS_TOKEN_NOT_FOUND)
            REFRESH_TOKEN -> refreshTokenEntityRepository.findByTokenValue(token)?.authorization
                ?: throw RefreshTokenNotFoundException(RefreshTokenNotFoundException.REFRESH_TOKEN_NOT_FOUND)
            null -> authorizationEntityRepository.findAuthorizationByTokens(token)
                ?: throw AuthorizationNotFoundException(AuthorizationNotFoundException.AUTHORIZATION_NOT_FOUND)
            else -> throw IllegalArgumentException("UnExpectedException: Illegal Token Type Exception")
        }
    }
}
