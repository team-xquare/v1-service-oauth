package com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class TokenResponse(
    val accessToken: String,
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val accessExpiredAt: LocalDateTime,
    val refreshToken: String,
    @JsonSerialize(using = LocalDateTimeSerializer::class)
    val refreshExpiredAt: LocalDateTime
)

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
    override fun serialize(
        value: LocalDateTime?,
        gen: JsonGenerator?,
        serializers: SerializerProvider?
    ) {
        gen?.writeString(value?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}