package com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid

import com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.exceptions.IllegalUuidStringException
import java.util.UUID

object UuidUtils {
    fun String.toUUID(): UUID {
        return try {
            UUID.fromString(this)
        } catch (exception: IllegalArgumentException) {
            throw IllegalUuidStringException("Failed to convert $this to UUID")
        }
    }
}
