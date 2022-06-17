package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class AuthorizationNotFoundException(
    errorMessage: String
) : BaseException(errorMessage, 404) {
    companion object {
        const val AUTHORIZATION_CODE_NOT_FOUND = "Authorization Code Not Found"
        const val AUTHORIZATION_STATE_NOT_FOUND = "Authorization State Not Found"
        const val AUTHORIZATION_NOT_FOUND = "Authorization Not Found"
        const val AUTHORIZATION_ID_NOT_FOUND = "Authorization Id Not Found"
    }
}
