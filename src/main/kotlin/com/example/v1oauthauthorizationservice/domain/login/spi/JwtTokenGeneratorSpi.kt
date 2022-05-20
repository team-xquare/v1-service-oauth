package com.example.v1oauthauthorizationservice.domain.login.spi

import com.example.v1oauthauthorizationservice.domain.login.spi.dtos.TokensSpiDto

interface JwtTokenGeneratorSpi {
    fun generateJwtTokenString(subject: String): TokensSpiDto
}
