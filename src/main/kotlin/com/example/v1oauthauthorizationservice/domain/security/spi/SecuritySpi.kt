package com.example.v1oauthauthorizationservice.domain.security.spi

import java.util.UUID

interface SecuritySpi {
    fun getCurrentUserId(): UUID
    fun encode(rawString: String): String
}
