package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FailedToAuthorizeException(
    errorMessage: String
) : BaseException(errorMessage, 401) {
    companion object {
        const val FULL_AUTHORIZATION_NEEDED = "Full Authorization Needed Exception. Please check you token."
        const val AUTHORIZATION_FAILED_EXCEPTION = "Authorization Failed"
    }
}
