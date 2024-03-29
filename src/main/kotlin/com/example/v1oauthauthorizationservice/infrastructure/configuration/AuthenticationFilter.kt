package com.example.v1oauthauthorizationservice.infrastructure.configuration

import com.example.v1oauthauthorizationservice.global.config.jwt.JwtTokenResolver
import com.example.v1oauthauthorizationservice.global.config.jwt.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
@Order(-100)
class AuthenticationFilter(
    private val tokenResolver: JwtTokenResolver,
    private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        tokenResolver.resolveToken(request)
            ?.let {
                SecurityContextHolder.getContext().authentication = tokenProvider.getAuthentication(it)
            }
        filterChain.doFilter(request, response)
    }
}
