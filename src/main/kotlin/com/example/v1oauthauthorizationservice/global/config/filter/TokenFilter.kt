package com.example.v1oauthauthorizationservice.global.config.filter

import com.example.v1oauthauthorizationservice.global.config.jwt.JwtTokenResolver
import com.example.v1oauthauthorizationservice.global.config.jwt.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenFilter(
    private val tokenResolver: JwtTokenResolver,
    private val tokenProvider: TokenProvider
): OncePerRequestFilter() {

    @Throws(Exception::class)
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