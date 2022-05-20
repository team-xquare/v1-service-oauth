package com.example.v1oauthauthorizationservice.domain.login.spi

interface PasswordMatcherSpi {
    fun isRawPasswordAndEncodedPasswordMatches(rawPassword: String, encodedPassword: String): Boolean
}
