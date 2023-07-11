package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.filter

import com.example.v1oauthauthorizationservice.domain.exception.BaseException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.basic.InternalServerError
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.oauth2.AuthenticationException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.oauth2.AuthorizationException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.utils.ExceptionResponseUtils.convertToBaseExceptionIfPossible
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.utils.ExceptionResponseUtils.sendErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.oauth2.core.OAuth2AuthorizationException
import org.springframework.web.filter.OncePerRequestFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.stereotype.Component

@Component
class CustomExceptionHandlerFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            e.printStackTrace()
            when (val exception = e.convertToBaseExceptionIfPossible()) {
                is BaseException -> exception
                is OAuth2AuthorizationException -> AuthorizationException(exception.message)
                is OAuth2AuthenticationException -> AuthenticationException(exception.message)
                else -> InternalServerError(exception.message)
            }.sendErrorResponse(response, objectMapper)
        }
    }
}
