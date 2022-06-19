package com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class IllegalUuidStringException(
    errorMessage: String
) : BaseException(errorMessage, 400)
