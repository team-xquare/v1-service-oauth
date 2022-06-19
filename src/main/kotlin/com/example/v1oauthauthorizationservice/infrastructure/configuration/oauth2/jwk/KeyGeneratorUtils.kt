package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.jwk

import java.security.KeyPair
import java.security.KeyPairGenerator

object KeyGeneratorUtils {
    fun generateRsaKey(): KeyPair {
        return KeyPairGenerator.getInstance("RSA")
            .apply {
                initialize(2048)
            }.generateKeyPair()
    }
}
