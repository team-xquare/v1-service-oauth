package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.jwk

import java.security.KeyPair
import java.security.KeyPairGenerator
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object KeyGeneratorUtils {
    fun generateSecretKey(): SecretKey {
        return KeyGenerator.getInstance("HmacSha256").generateKey()
    }

    fun generateRsaKey(): KeyPair {
        return KeyPairGenerator.getInstance("RSA")
            .apply {
                initialize(2048)
            }.generateKeyPair()
    }
}
