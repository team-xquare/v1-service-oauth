package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AccessTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AccessTokenEntityRepository : JpaRepository<AccessTokenEntity, UUID> {
    fun findByTokenValue(tokenValue: String): AccessTokenEntity?
}
