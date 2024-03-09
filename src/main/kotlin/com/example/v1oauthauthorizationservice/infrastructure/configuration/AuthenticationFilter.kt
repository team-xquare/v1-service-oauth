package com.example.v1oauthauthorizationservice.infrastructure.configuration

import com.example.v1oauthauthorizationservice.global.config.jwt.JwtTokenResolver
import com.example.v1oauthauthorizationservice.global.config.jwt.TokenProvider
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions.AccessTokenNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions.AccessTokenNotFoundException.Companion.ACCESS_TOKEN_NOT_FOUND
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions.AuthorizationNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.exceptions.AuthorizationNotFoundException.Companion.AUTHORIZATION_NOT_FOUND
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.AccessTokenEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.AuthorizationAttributeEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.repository.AuthorizationEntityRepository
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.utils.toOAuth2AccessToken
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication
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
