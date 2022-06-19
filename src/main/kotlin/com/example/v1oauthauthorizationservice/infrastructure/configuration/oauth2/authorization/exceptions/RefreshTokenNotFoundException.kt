package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class RefreshTokenNotFoundException(
    errorMessage: String
) : BaseException(errorMessage, 404) {
    companion object {
        const val REFRESH_TOKEN_NOT_FOUND = "Refresh Token Not Found"
    }
}
