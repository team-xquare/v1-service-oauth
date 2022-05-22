package com.example.v1oauthauthorizationservice.domain.exception

interface BaseExceptionProperty {
    val errorMessage: String?
    val statusCode: Int
}
