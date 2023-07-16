package com.example.v1oauthauthorizationservice.domain.oauth.spi

import com.example.v1oauthauthorizationservice.domain.oauth.domain.RegisteredClientDto

interface RegisteredClientSpi {
    fun save(registeredClientDto: RegisteredClientDto)
    fun findByClientId(clientId: String): RegisteredClientDto
    fun existsByClientId(clientId: String): Boolean
}
