package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FailedToGenerateTokenException(
    errorMessage: String,
    statusCode: Int
) : BaseException(errorMessage, statusCode) {
    companion object {
        const val ALREADY_USED_CODE = "this Authorization Code Already Used. Please try again"
    }
}
