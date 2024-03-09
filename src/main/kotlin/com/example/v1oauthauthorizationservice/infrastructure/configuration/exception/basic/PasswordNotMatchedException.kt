package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.basic

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class PasswordNotMatchedException(
    errorMessage: String?
) : BaseException(errorMessage, 400)