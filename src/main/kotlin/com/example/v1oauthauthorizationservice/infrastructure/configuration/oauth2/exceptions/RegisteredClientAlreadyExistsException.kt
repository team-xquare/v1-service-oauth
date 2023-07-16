package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class RegisteredClientAlreadyExistsException(
    errorMessage: String
) : BaseException(errorMessage, 409) {
    companion object {
        const val CLIENT_ID_ALREADY_EXISTS_MESSAGE = "Registered Client By This Client Id Already Exists Exception"
    }
}
