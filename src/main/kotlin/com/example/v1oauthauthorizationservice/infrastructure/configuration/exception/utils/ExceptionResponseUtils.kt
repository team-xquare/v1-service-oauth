package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.utils

import com.example.v1oauthauthorizationservice.domain.exception.BaseException
import com.example.v1oauthauthorizationservice.domain.exception.BaseExceptionProperty
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.dtos.ExceptionResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import javax.servlet.http.HttpServletResponse

object ExceptionResponseUtils {
    fun BaseExceptionProperty.sendErrorResponse(response: HttpServletResponse, objectMapper: ObjectMapper) {
        val exceptionResponse = ExceptionResponse(
            errorMessage = this.errorMessage,
            statusCode = this.statusCode
        )

        val exceptionResponseJsonString = objectMapper.writeValueAsString(exceptionResponse)

        response.status = this.statusCode
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(exceptionResponseJsonString)
    }

    fun Exception.convertToBaseExceptionIfPossible() =
        if (this.cause is BaseException) {
            this.cause!!
        } else {
            this
        }
}
