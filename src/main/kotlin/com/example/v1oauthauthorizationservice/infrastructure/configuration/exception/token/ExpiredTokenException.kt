package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.token

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class ExpiredTokenException (
    errorMessage: String?
) : BaseException(errorMessage, 401)
