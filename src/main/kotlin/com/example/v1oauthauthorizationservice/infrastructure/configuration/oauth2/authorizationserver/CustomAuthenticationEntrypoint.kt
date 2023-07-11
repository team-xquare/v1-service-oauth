package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorizationserver

import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.basic.InternalServerError
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.utils.ExceptionResponseUtils.convertToBaseExceptionIfPossible
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.utils.ExceptionResponseUtils.sendErrorResponse
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.exceptions.FailedToAuthorizeException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

class CustomAuthenticationEntrypoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        authException.printStackTrace()
        when (val exception = authException.convertToBaseExceptionIfPossible()) {
            is InsufficientAuthenticationException -> FailedToAuthorizeException(FailedToAuthorizeException.FULL_AUTHORIZATION_NEEDED)
                .sendErrorResponse(response, objectMapper)
            is AuthenticationException -> FailedToAuthorizeException(FailedToAuthorizeException.AUTHORIZATION_FAILED_EXCEPTION)
                .sendErrorResponse(response, objectMapper)
            else -> InternalServerError(exception.message).sendErrorResponse(response, objectMapper)
        }
    }
}
