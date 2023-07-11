package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class RegisteredClientNotFoundException(
    errorMessage: String
) : BaseException(errorMessage, 404) {
    companion object {
        const val ID_NOT_FOUND_MESSAGE = "Registered Client By This Id Not Found Exception"
        const val CLIENT_ID_NOT_FOUND_MESSAGE = "Registered Client By This Client Id Not Found Exception"
    }
}
