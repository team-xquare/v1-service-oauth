package com.example.v1oauthauthorizationservice.infrastructure.user.spi

import com.example.v1oauthauthorizationservice.domain.login.spi.PasswordMatcherSpi
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Repository

@Repository
class PasswordMatcherSpiImpl(
    private val passwordEncoder: PasswordEncoder
): PasswordMatcherSpi {
    override fun isRawPasswordAndEncodedPasswordMatches(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}
