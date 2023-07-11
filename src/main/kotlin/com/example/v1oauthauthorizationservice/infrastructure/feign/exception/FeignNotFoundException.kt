package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignNotFoundException : BaseException(
    errorMessage = "Feign Not Found",
    statusCode = 500
)