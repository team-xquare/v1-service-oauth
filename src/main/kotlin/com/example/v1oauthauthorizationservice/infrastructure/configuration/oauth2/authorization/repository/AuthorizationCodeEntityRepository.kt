package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface AuthorizationCodeEntityRepository : JpaRepository<AuthorizationCodeEntity, UUID> {
    fun findByCodeValue(codeValue: String): AuthorizationCodeEntity?
    fun findByState(state: String): AuthorizationCodeEntity?
}
