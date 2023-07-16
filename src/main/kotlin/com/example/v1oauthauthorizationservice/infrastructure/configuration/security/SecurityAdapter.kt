package com.example.v1oauthauthorizationservice.infrastructure.configuration.security

import com.example.v1oauthauthorizationservice.domain.security.spi.SecuritySpi
import java.util.UUID
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SecurityAdapter(
    private val passwordEncoder: PasswordEncoder
) : SecuritySpi {

    override fun getCurrentUserId(): UUID =
        UUID.fromString(SecurityContextHolder.getContext().authentication.name)

    override fun encode(rawString: String): String =
        passwordEncoder.encode(rawString)
}
