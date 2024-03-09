package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.filter

import com.example.v1oauthauthorizationservice.domain.exception.BaseException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.error.data.BindErrorResponse
import com.example.v1oauthauthorizationservice.infrastructure.configuration.error.data.ErrorResponse
import org.springframework.core.convert.ConversionFailedException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler


@RestControllerAdvice
class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    protected fun handleBindException(e: BindException): BindErrorResponse = ErrorResponse.of(e)

    @ExceptionHandler(BaseException::class)
    protected fun customExceptionHandle(e: BaseException): ResponseEntity<ErrorResponse> =
        ResponseEntity(ErrorResponse(HttpStatus.BAD_REQUEST, e.localizedMessage), HttpStatus.BAD_REQUEST)

    @ExceptionHandler(HttpMessageNotReadableException::class)
    protected fun handleHttpMessageNotReadable(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(HttpStatus.BAD_REQUEST, e.message ?: e.localizedMessage),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(IllegalStateException::class)
    protected fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(ConversionFailedException::class)
    protected fun handleConversionFailedException(e: ConversionFailedException): ResponseEntity<ErrorResponse> =
        ResponseEntity(
            ErrorResponse(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
            HttpStatus.BAD_REQUEST
        )
}