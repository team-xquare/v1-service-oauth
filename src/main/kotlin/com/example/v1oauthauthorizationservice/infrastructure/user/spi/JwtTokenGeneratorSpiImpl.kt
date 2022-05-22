package com.example.v1oauthauthorizationservice.infrastructure.user.spi

import com.example.v1oauthauthorizationservice.domain.login.spi.JwtTokenGeneratorSpi
import com.example.v1oauthauthorizationservice.domain.login.spi.dtos.TokensSpiDto
import com.example.v1oauthauthorizationservice.infrastructure.configuration.jwt.BaseTokenProperties
import com.example.v1oauthauthorizationservice.infrastructure.configuration.jwt.JwtProperties
import com.example.v1oauthauthorizationservice.infrastructure.configuration.jwt.JwtUtils
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class JwtTokenGeneratorSpiImpl(
    private val jwtProperties: JwtProperties
) : JwtTokenGeneratorSpi {
    override fun generateJwtTokenString(subject: String): TokensSpiDto {
        val accessTokenProperties = jwtProperties.accessTokenProperties
        val refreshTokenProperties = jwtProperties.refreshTokenProperties

        val accessToken = generateToken(subject, accessTokenProperties)
        val refreshToken = generateToken(subject, refreshTokenProperties)

        return TokensSpiDto(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    private fun generateToken(subject: String, tokenProperties: BaseTokenProperties): String {
        return JwtUtils.generateJwtTokenWithSubjectAndExpiration(
            subject = subject,
            expiration = LocalDateTime.now().plusHours(tokenProperties.expirationAsHour.toLong()),
            type = tokenProperties.type,
            secretKey = jwtProperties.secretKey
        )
    }
}
