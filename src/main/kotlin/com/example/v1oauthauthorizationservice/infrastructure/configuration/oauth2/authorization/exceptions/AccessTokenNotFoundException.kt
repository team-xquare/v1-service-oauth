package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class AccessTokenNotFoundException(
    errorMessage: String
) : BaseException(errorMessage, 404) {
    companion object {
        const val ACCESS_TOKEN_NOT_FOUND = "Access Token Not Found"
    }
}
