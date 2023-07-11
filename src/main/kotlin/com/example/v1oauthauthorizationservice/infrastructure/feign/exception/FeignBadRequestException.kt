package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignBadRequestException : BaseException(
    errorMessage = FEIGN_BAD_REQUEST,
    statusCode = 400
) {
    companion object {
        const val FEIGN_BAD_REQUEST = "Feign Bad Request"
    }
}
