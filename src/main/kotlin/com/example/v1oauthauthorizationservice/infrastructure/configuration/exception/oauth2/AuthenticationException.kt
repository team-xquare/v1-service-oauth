package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.oauth2

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class AuthenticationException(
    errorMessage: String?
) : BaseException(errorMessage, 401)
