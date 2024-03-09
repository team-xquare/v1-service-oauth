package com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.filter

import com.example.v1oauthauthorizationservice.domain.exception.BaseException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.error.data.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ExceptionHandlerFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    private fun sendErrorResponse(
        statusCode: Int,
        message: String,
        response: HttpServletResponse
    ) {
        response.let {
            it.status = statusCode
            it.contentType = MediaType.APPLICATION_JSON_VALUE
            it.characterEncoding = "UTF-8"
        }
        objectMapper.writeValue(
            response.writer,
            ErrorResponse.of(statusCode, message)
        )
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: BaseException) {
            sendErrorResponse(e.statusCode, e.message ?: "error", response)
        } catch (e: Exception) {
            e.printStackTrace()
            sendErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error", response)
        }
    }
}