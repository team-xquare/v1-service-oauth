package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationAttributeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthorizationAttributeEntityRepository : JpaRepository<AuthorizationAttributeEntity, UUID> {
    fun findByAuthorizationId(authorizationId: UUID): List<AuthorizationAttributeEntity>
}
