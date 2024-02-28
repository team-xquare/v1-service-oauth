package com.example.v1oauthauthorizationservice.infrastructure.configuration

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
import java.util.*

@Component
@Order(-100)
class AuthenticationFilter(
    private val accessTokenEntityRepository: AccessTokenEntityRepository,
    private val authenticationEntityRepository: AuthorizationEntityRepository,
    private val authorizationAttributeEntityRepository: AuthorizationAttributeEntityRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI.contains("/oauth2/token")) {
            filterChain.doFilter(request, response)
            return
        }

        request.getParameter("access_token")?.let { accessToken ->
            setAuthenticationByAccessToken(accessToken)
            filterChain.doFilter(request, response)
            return
        }
        setAuthenticationByHeader(request)
        filterChain.doFilter(request, response)
    }

    private fun setAuthenticationByHeader(request: HttpServletRequest) {
        var requestUserId = request.getHeader("Request-User-Id") ?: UUID.randomUUID().toString()
        var requestUserAuthority = request.getHeader("Request-User-Authorities") ?: "STU"
        var requestUserRole = request.getHeader("Request-User-Role") ?: "STU"

        val authorityList = requestUserAuthority.toList()
        val simpleGrantedAuthorities = if(authorityList.isNotEmpty()) {
            buildRequestAuthoritiesAndRole(requestUserRole, authorityList)
                .map { SimpleGrantedAuthority(it) }
        } else {
            listOf<SimpleGrantedAuthority>()
        }

        if (simpleGrantedAuthorities.isNotEmpty()) {
            val user = User(
                requestUserId,
                "",
                simpleGrantedAuthorities
            )
            val authentication = UsernamePasswordAuthenticationToken(user, "", user.authorities)

            SecurityContextHolder.getContext().authentication = authentication
        }
    }

    private fun buildRequestAuthoritiesAndRole(
        requestUserRole: String,
        requestUserAuthorities: List<String>
    ): List<String> {
        val authoritiesAndRoles = mutableListOf("ROLE_$requestUserRole")
        requestUserAuthorities.forEach { authoritiesAndRoles.add(it) }
        return authoritiesAndRoles
    }

    private fun setAuthenticationByAccessToken(accessToken: String) {
        val tokenEntity = accessTokenEntityRepository.findByTokenValue(accessToken)
            ?: throw AccessTokenNotFoundException(ACCESS_TOKEN_NOT_FOUND)

        val authorization = authenticationEntityRepository.findByIdOrNull(tokenEntity.authorization.id)
            ?: throw AuthorizationNotFoundException(AUTHORIZATION_NOT_FOUND)

        val attributes = authorizationAttributeEntityRepository.findByAuthorizationId(authorization.id!!).associate { it.attributeKey to it.attributeValue }
        val authorities = listOf<GrantedAuthority>()

        SecurityContextHolder.getContext().authentication = BearerTokenAuthentication(
            DefaultOAuth2AuthenticatedPrincipal(attributes, authorities),
            tokenEntity.toOAuth2AccessToken(),
            authorities
        )
    }

    private fun String.toList(): List<String> {
        return this.removeSurrounding("[", "]")
            .replace(" ", "")
            .split(",")
    }
}
