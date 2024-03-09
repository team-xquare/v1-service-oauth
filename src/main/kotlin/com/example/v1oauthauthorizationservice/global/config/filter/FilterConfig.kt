package com.example.v1oauthauthorizationservice.global.config.filter

import com.example.v1oauthauthorizationservice.global.config.jwt.JwtTokenResolver
import com.example.v1oauthauthorizationservice.global.config.jwt.TokenProvider
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.filter.CustomExceptionHandlerFilter
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class FilterConfig(
    private val tokenProvider: TokenProvider,
    private val tokenResolver: JwtTokenResolver,
    private val customExceptionHandlerFilter: CustomExceptionHandlerFilter
): SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity.addFilterBefore(TokenFilter(tokenResolver, tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
        httpSecurity.addFilterBefore(customExceptionHandlerFilter, TokenFilter::class.java)
    }
}