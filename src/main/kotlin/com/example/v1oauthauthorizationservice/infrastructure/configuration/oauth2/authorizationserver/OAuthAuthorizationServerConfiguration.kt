package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorizationserver

import com.example.v1oauthauthorizationservice.infrastructure.configuration.AuthenticationFilter
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.filter.CustomExceptionHandlerFilter
import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.jwk.JwkUtils
import com.example.v1oauthauthorizationservice.infrastructure.configuration.properties.service.ServiceProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter

@Configuration
class OAuthAuthorizationServerConfiguration(
    private val objectMapper: ObjectMapper
) {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun providerSettings(serviceProperties: ServiceProperties): ProviderSettings {
        return ProviderSettings.builder()
            .issuer("http://localhost:8080") // TODO 서비스 주소
            .jwkSetEndpoint("/jwks")
            .oidcUserInfoEndpoint("/oauth2/userinfo")
            .tokenEndpoint("/oauth2/token")
            .authorizationEndpoint("/oauth2/authorize")
            .tokenIntrospectionEndpoint("/tokens")
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    fun authorizationServerSecurityFilterChain(
        httpSecurity: HttpSecurity,
        providerSettings: ProviderSettings
    ): SecurityFilterChain {
        val oAuth2AuthorizationServerConfigurer = OAuth2AuthorizationServerConfigurer<HttpSecurity>()

        httpSecurity
            .cors().disable()
            .csrf().disable()

        httpSecurity
            .apply(oAuth2AuthorizationServerConfigurer).and()
            .formLogin().disable()
            .httpBasic().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        httpSecurity.authorizeRequests()
            .antMatchers(HttpMethod.POST, "/oauth2/token").permitAll()
            .antMatchers(HttpMethod.POST, "/oauth2/authorize").permitAll()
            .antMatchers("/v1/users/login").permitAll()
            .antMatchers(HttpMethod.GET, "/jwk").permitAll()
            .antMatchers("/tokens").permitAll()
            .anyRequest().authenticated()

        httpSecurity
            .addFilterAfter(
                CustomExceptionHandlerFilter(objectMapper),
                LogoutFilter::class.java
            )
            .addFilterAfter(
                AuthenticationFilter(providerSettings),
                CustomExceptionHandlerFilter::class.java
            )

        return httpSecurity.build()
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey: RSAKey = JwkUtils.generateRsaKey()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource<SecurityContext> { jwkSelector, _ ->
            jwkSelector.select(
                jwkSet
            )
        }
    }
}
