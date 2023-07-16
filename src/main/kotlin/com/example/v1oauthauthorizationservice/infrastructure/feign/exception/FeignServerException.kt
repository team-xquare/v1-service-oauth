package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignServerException : BaseException(
    errorMessage = FEIGN_SERVER_ERROR,
    statusCode = 500
) {
    companion object {
        const val FEIGN_SERVER_ERROR = "Feign Server Error"
    }
}
