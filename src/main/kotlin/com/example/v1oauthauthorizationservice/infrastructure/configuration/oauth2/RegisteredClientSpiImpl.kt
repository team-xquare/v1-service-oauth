package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2

import com.example.v1oauthauthorizationservice.domain.oauth.domain.RegisteredClientDto
import com.example.v1oauthauthorizationservice.domain.oauth.spi.RegisteredClientSpi
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions.RegisteredClientNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository.RedirectUriEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.repository.RegisteredClientEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.mapper.RegisteredClientMapper
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class RegisteredClientSpiImpl(
    private val registeredClientEntityRepository: RegisteredClientEntityRepository,
    private val redirectUriEntityRepository: RedirectUriEntityRepository,
    private val registeredClientMapper: RegisteredClientMapper
) : RegisteredClientSpi {

    override fun save(registeredClientDto: RegisteredClientDto) {
        val registeredClientEntityToSave = registeredClientMapper.toEntity(registeredClientDto)
        val savedRegisteredClientEntity = registeredClientEntityRepository.save(registeredClientEntityToSave)

        redirectUriEntityRepository.deleteByRegisteredClientId(savedRegisteredClientEntity.id)
        redirectUriEntityRepository.saveAll(
            registeredClientDto.redirectUris
                .map { it.toRedirectUriEntity(savedRegisteredClientEntity) }
        )
    }

    private fun String.toRedirectUriEntity(registeredClientEntity: RegisteredClientEntity) =
        RedirectUriEntity(
            uri = this,
            registeredClient = registeredClientEntity
        )

    override fun getByUserId(userId: UUID) =
        registeredClientEntityRepository.findByUserId(userId)
            .map { registeredClientMapper.toDto(it) }

    override fun findByClientId(clientId: String) =
        registeredClientMapper.toDto(
            registeredClientEntityRepository.findByClientId(clientId)
                ?: throw RegisteredClientNotFoundException(RegisteredClientNotFoundException.CLIENT_ID_NOT_FOUND_MESSAGE)
        )

    override fun existsByClientId(clientId: String) =
        registeredClientEntityRepository.existsByClientId(clientId)
}
