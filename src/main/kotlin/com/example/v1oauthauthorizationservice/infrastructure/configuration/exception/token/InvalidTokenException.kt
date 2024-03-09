package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.token

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class InvalidTokenException(
    errorMessage: String?
) : BaseException(errorMessage, 401)
