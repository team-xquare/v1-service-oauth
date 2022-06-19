package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.basic

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class InternalServerError(
    errorMessage: String?
) : BaseException(errorMessage, 500)
