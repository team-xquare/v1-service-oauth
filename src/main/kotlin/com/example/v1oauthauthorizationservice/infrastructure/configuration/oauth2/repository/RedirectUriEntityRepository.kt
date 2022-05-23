package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.RedirectUriEntity
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface RedirectUriEntityRepository : CrudRepository<RedirectUriEntity, UUID> {
    fun findAllByRegisteredClientId(registeredClientId: UUID): List<RedirectUriEntity>
}
