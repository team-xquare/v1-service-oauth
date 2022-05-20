package com.example.v1oauthauthorizationservice.domain.exception

abstract class BaseException(
    override val errorMessage: String?,
    override val statusCode: Int
) : RuntimeException(errorMessage), BaseExceptionProperty {
    override fun fillInStackTrace(): Throwable {
        return this
    }
}
