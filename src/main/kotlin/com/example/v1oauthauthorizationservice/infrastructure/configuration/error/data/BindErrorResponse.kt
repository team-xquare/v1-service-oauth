package com.example.v1oauthauthorizationservice.infrastructure.configuration.error.data

import org.springframework.http.HttpStatus

data class BindErrorResponse(
    val status: HttpStatus,
    val fieldError: List<Map<String, String?>>
)