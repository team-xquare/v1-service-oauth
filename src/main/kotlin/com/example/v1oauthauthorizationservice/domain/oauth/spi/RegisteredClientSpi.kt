package com.example.v1oauthauthorizationservice.domain.oauth.spi

import com.example.v1oauthauthorizationservice.domain.oauth.domain.RegisteredClientDto
import java.util.UUID

interface RegisteredClientSpi {
    fun save(registeredClientDto: RegisteredClientDto)
    fun getByUserId(userId: UUID): List<RegisteredClientDto>
    fun findByClientId(clientId: String): RegisteredClientDto
    fun existsByClientId(clientId: String): Boolean
}
