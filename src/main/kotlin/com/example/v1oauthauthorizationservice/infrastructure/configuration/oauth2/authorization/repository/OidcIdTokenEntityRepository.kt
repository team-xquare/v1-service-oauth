package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.OidcIdTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OidcIdTokenEntityRepository : JpaRepository<OidcIdTokenEntity, UUID>
