package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationEntity
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AuthorizationEntityRepository : JpaRepository<AuthorizationEntity, UUID> {

    @Query(
        """select authorization from AuthorizationEntity authorization
                left join authorization.accessTokenEntity
                left join authorization.refreshTokenEntity
                left join authorization.oidcIdTokenEntity
                left join authorization.authorizationCodeEntity
            where authorization.accessTokenEntity.tokenValue = :token
                or authorization.refreshTokenEntity.tokenValue = :token
                or authorization.oidcIdTokenEntity.tokenValue = :token
                or authorization.authorizationCodeEntity.codeValue = :token
        """
    )
    fun findAuthorizationByTokens(@Param("token") tokenValue: String): AuthorizationEntity?
}
