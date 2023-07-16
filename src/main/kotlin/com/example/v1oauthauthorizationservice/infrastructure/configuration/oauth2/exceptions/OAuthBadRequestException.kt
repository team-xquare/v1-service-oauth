package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class OAuthBadRequestException(
    errorMessage: String
) : BaseException(errorMessage, 400) {
    companion object {
        const val REDIRECT_URI_NOT_VALID = "invalid_redirect_uri (e.g. RegisteredClient Doesn't Contains Uri)"
        const val SCOPES_NOT_VALID = "invalid_scope"
    }
}
