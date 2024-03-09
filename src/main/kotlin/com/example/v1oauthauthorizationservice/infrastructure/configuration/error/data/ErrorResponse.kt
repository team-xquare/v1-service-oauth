package com.example.v1oauthauthorizationservice.infrastructure.configuration.error.data

import com.example.v1oauthauthorizationservice.domain.exception.BaseException
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.FieldError

data class ErrorResponse(
    val status: HttpStatus,
    val message: String
) {
    companion object {

        fun of(statusCode: Int, message: String) = ErrorResponse(
            HttpStatus.valueOf(statusCode),
            message
        )

        fun of(e: BindException): BindErrorResponse {

            val errorMap = HashMap<String, String?>()

            for (error: FieldError in e.fieldErrors) {
                errorMap[error.field] = error.defaultMessage
            }

            return BindErrorResponse(HttpStatus.BAD_REQUEST, listOf(errorMap))
        }
    }
}
