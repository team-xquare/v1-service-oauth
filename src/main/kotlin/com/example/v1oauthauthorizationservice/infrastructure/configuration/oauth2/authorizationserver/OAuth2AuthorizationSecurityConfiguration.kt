package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorizationserver

import com.example.v1oauthauthorizationservice.infrastructure.configuration.AuthenticationFilter
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.filter.CustomExceptionHandlerFilter
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.jwk.JwkUtils
import com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.UuidUtils.toUUID
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter

@Configuration
class OAuth2AuthorizationSecurityConfiguration(
    private val userRepository: UserRepository,
    private val objectMapper: ObjectMapper,
    private val customOAuth2AuthorizationService: CustomOAuth2AuthorizationService,
    private val customExceptionHandlerFilter: CustomExceptionHandlerFilter,
    private val authenticationFilter: AuthenticationFilter
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity)

        httpSecurity
            .setCustomOAuth2AuthorizationConfigurer().and()
            .exceptionHandling()
            .authenticationEntryPoint(CustomAuthenticationEntrypoint(objectMapper)).and()
            .oauth2ResourceServer { obj: OAuth2ResourceServerConfigurer<HttpSecurity> -> obj.jwt() }

        httpSecurity
            .addFilterAfter(
                customExceptionHandlerFilter,
                LogoutFilter::class.java
            )
            .addFilterAfter(
                authenticationFilter,
                CustomExceptionHandlerFilter::class.java
            )

        return httpSecurity.build()
    }

    private fun HttpSecurity.setCustomOAuth2AuthorizationConfigurer() =
        getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .authorizationService(customOAuth2AuthorizationService)
            .oidc {
                it.userInfoEndpoint { configurer ->
                    configurer.userInfoMapper(getUserInfoByAuthenticationContext())
                }
            }

    private fun getUserInfoByAuthenticationContext() = { authenticationContext: OidcUserInfoAuthenticationContext ->
        val userInfo = userRepository.findUserById(
            userId = authenticationContext.authorization.principalName.toUUID()
        )
        OidcUserInfo(objectMapper.convertValue(userInfo, Map::class.java) as Map<String, Any>)
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings
            .builder()
            .authorizationEndpoint("/oauth2/authorize")
            .tokenEndpoint("/oauth2/token")
            .oidcUserInfoEndpoint("/userinfo")
            .jwkSetEndpoint("/jwks")
            .build()
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext?>?): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = JwkUtils.generateRsaKey()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource<SecurityContext> { jwkSelector, _ ->
            jwkSelector.select(
                jwkSet
            )
        }
    }
}
