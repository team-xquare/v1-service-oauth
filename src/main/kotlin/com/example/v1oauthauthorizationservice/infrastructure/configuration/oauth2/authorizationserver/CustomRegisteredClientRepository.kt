package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorizationserver

import com.example.v1oauthauthorizationservice.infrastructure.configuration.cache.CacheNames
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions.RegisteredClientNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository.RegisteredClientEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.UuidUtils.toUUID
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.mapper.RegisteredClientMapper
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Component

@Component
class CustomRegisteredClientRepository(
    private val registeredClientEntityRepository: RegisteredClientEntityRepository,
    private val registeredClientMapper: RegisteredClientMapper
) : RegisteredClientRepository {

    override fun save(registeredClient: RegisteredClient) {
        // Unused method
    }

    @Cacheable(cacheNames = [CacheNames.REGISTERED_CLIENT_CACHE_NAME], key = "#id")
    override fun findById(id: String): RegisteredClient {
        val registeredClientEntity = registeredClientEntityRepository.findByIdOrNull(id.toUUID())
            ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.ID_NOT_FOUND_MESSAGE)
        return registeredClientMapper.toClient(registeredClientEntity)
    }

    @Cacheable(cacheNames = [CacheNames.REGISTERED_CLIENT_CACHE_NAME], key = "#clientId")
    override fun findByClientId(clientId: String): RegisteredClient {
        val registeredClientEntity = registeredClientEntityRepository.findByClientId(clientId)
            ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.CLIENT_ID_NOT_FOUND_MESSAGE)
        return registeredClientMapper.toClient(registeredClientEntity)
    }
}
