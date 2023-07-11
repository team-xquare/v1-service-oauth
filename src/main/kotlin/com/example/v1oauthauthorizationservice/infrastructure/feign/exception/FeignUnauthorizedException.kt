package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignUnauthorizedException : BaseException(
    errorMessage = FEIGN_UNAUTHORIZED,
    statusCode = 401
) {
    companion object {
        const val FEIGN_UNAUTHORIZED = "Feign Unauthorized"
    }
}
