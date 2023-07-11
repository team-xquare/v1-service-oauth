package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignForbiddenException : BaseException(
    errorMessage = "Feign Forbidden",
    statusCode = 403
)