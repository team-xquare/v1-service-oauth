package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.RegisteredClientEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RegisteredClientEntityRepository : CrudRepository<RegisteredClientEntity, UUID> {
    fun findByClientId(clientId: String): RegisteredClientEntity?
    fun findByUserId(userId: UUID): List<RegisteredClientEntity>
    fun existsByClientId(clientId: String): Boolean
}
