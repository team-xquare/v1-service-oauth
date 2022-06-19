package com.example.v1oauthauthorizationservice.infrastructure.configuration

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter(
    providerSettings: ProviderSettings
) : OncePerRequestFilter() {

    private val authorizeEndpoint: String = providerSettings.authorizationEndpoint

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (!request.requestURI.equals(authorizeEndpoint)) {
            filterChain.doFilter(request, response)
            return
        }

        val requestUserId = request.getHeader("Request-User-Id")
        val requestUserAuthority = request.getHeader("Request-User-Authority")
        val requestUserRole = request.getHeader("Request-User-Role")

        val authorities = requestUserAuthority.toList()
        val authoritiesAndRoles = buildRequestAuthoritiesAndRole(requestUserRole, authorities)
        val simpleGrantedAuthorities = authoritiesAndRoles.map { SimpleGrantedAuthority(it) }

        val user = User(
            requestUserId,
            "",
            simpleGrantedAuthorities
        )

        val authentication = UsernamePasswordAuthenticationToken(user, "", user.authorities)

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    private fun buildRequestAuthoritiesAndRole(
        requestUserRole: String,
        requestUserAuthorities: List<String>
    ): List<String> {
        val authoritiesAndRoles = mutableListOf("ROLE_$requestUserRole")
        requestUserAuthorities.forEach { authoritiesAndRoles.add(it) }
        return requestUserAuthorities
    }

    private fun String.toList(): List<String> {
        return this.removeSurrounding("[", "]")
            .replace(" ", "")
            .split(",")
    }
}
