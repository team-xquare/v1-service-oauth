package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignForbiddenException : BaseException(
    errorMessage = FEIGN_FORBIDDEN,
    statusCode = 403
) {
    companion object {
        const val FEIGN_FORBIDDEN = "Feign Forbidden"
    }
}
