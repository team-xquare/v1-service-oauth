package com.example.v1oauthauthorizationservice.global.config.jwt

import com.example.v1oauthauthorizationservice.global.config.security.principal.AuthDetails
import com.example.v1oauthauthorizationservice.global.config.security.principal.AuthDetailsService
import com.example.v1oauthauthorizationservice.global.env.jwt.TokenProperty
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.token.ExpiredTokenException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.token.InvalidTokenException
import com.example.v1oauthauthorizationservice.infrastructure.user.entity.RefreshToken
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.TokenResponse
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.RefreshTokenRepository
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class TokenProvider(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val property: TokenProperty,
    private val authDetailsService: AuthDetailsService
) {
    private fun generateAccessToken(sub: String): String {
        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, property.secretKey)
            .setSubject(sub)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time.plus(property.accessExp)))
            .compact()
    }

    private fun generateRefreshToken(sub: String): String {
        refreshTokenRepository.findByIdOrNull(sub)?.let {
            refreshTokenRepository.delete(it)
        }.apply {

        }

        val refreshToken = Jwts.builder()
            .signWith(SignatureAlgorithm.HS256, property.secretKey)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time.plus(property.refreshExp)))
            .compact()

        refreshTokenRepository.save(RefreshToken(refreshToken, sub))

        return refreshToken
    }

    fun receiveToken(sub: String) = TokenResponse(
        generateAccessToken(sub),
        getExp(property.accessExp),
        generateRefreshToken(sub),
        getExp(property.refreshExp)
    )

    private fun getExp(exp: Long) = LocalDateTime.now().withNano(0).plusSeconds(exp/1000)

    private fun getSubject(token: String): String {
        return try {
            Jwts.parser()
                .setSigningKey(property.secretKey)
                .parseClaimsJws(token).body.subject
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException("Expired token exception")
                else -> throw InvalidTokenException("Invalid token exception")
            }
        }
    }

    fun getAuthentication(token: String): Authentication {
        val subject = getSubject(token)

        val authDetails = authDetailsService.loadUserByUsername(subject) as AuthDetails

        return UsernamePasswordAuthenticationToken(authDetails, "", authDetails.authorities)
    }

    fun reissue(token: String): TokenResponse {
        val refreshToken = refreshTokenRepository.findByIdOrNull(token)
            ?: throw InvalidTokenException("Invalid token exception")

        return receiveToken(refreshToken.accountId)
    }
}