package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignBadRequestException : BaseException(
    errorMessage = "Feign Bad Request",
    statusCode = 400
)