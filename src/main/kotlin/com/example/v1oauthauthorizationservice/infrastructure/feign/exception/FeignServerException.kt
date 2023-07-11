package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignServerException : BaseException(
    errorMessage = "Feign server error",
    statusCode = 500
)