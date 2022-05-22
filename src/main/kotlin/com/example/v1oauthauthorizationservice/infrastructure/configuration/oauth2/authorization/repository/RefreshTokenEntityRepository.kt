package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RefreshTokenEntityRepository : JpaRepository<RefreshTokenEntity, UUID> {
    fun findByTokenValue(tokenValue: String): RefreshTokenEntity?
}
