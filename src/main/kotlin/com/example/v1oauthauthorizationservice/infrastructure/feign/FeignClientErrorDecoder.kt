package com.example.v1oauthauthorizationservice.infrastructure.feign

import com.example.v1oauthauthorizationservice.infrastructure.feign.exception.FeignBadRequestException
import com.example.v1oauthauthorizationservice.infrastructure.feign.exception.FeignForbiddenException
import com.example.v1oauthauthorizationservice.infrastructure.feign.exception.FeignNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.feign.exception.FeignServerException
import com.example.v1oauthauthorizationservice.infrastructure.feign.exception.FeignUnauthorizedException
import feign.FeignException
import feign.Response
import feign.codec.ErrorDecoder
import java.lang.Exception

class FeignClientErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): Exception? {
        if (response.status() >= 400) {
            when (response.status()) {
                401 -> throw FeignUnauthorizedException()
                403 -> throw FeignForbiddenException()
                404 -> throw FeignNotFoundException()
                500 -> throw FeignServerException()
                else -> throw FeignBadRequestException()
            }
        }
        return FeignException.errorStatus(methodKey, response)
    }
}
