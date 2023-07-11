package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignUnAuthorizedException : BaseException(
    errorMessage = "Feign Unauthorized",
    statusCode = 401
)