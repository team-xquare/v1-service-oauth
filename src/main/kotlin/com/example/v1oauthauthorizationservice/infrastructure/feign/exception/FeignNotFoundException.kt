package com.example.v1oauthauthorizationservice.infrastructure.feign.exception

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class FeignNotFoundException : BaseException(
    errorMessage = FEIGN_NOT_FOUND,
    statusCode = 404
) {
    companion object {
        const val FEIGN_NOT_FOUND = "Feign Not Found"
    }
}
