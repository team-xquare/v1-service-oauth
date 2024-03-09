package com.example.v1oauthauthorizationservice.infrastructure.configuration.security

import com.example.v1oauthauthorizationservice.global.config.filter.FilterConfig
import com.example.v1oauthauthorizationservice.global.config.jwt.JwtTokenResolver
import com.example.v1oauthauthorizationservice.global.config.jwt.TokenProvider
import com.example.v1oauthauthorizationservice.infrastructure.configuration.AuthenticationFilter
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.filter.ExceptionHandlerFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter

@EnableWebSecurity(debug = true)
@Configuration
class SecurityConfiguration(
    private val authenticationFilter: AuthenticationFilter,
    private val tokenProvider: TokenProvider,
    private val tokenResolver: JwtTokenResolver,
    private val exceptionHandlerFilter: ExceptionHandlerFilter
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun securityFilterChain(
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {

        httpSecurity
            .cors().disable()
            .csrf().disable()

        httpSecurity
            .formLogin().disable()
            .httpBasic().disable()
            .headers().frameOptions().disable().and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        httpSecurity.addFilterBefore(authenticationFilter, AuthorizationFilter::class.java)

        httpSecurity
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.GET, "/oauth2/client").authenticated()
                    .requestMatchers(HttpMethod.POST, "/oauth2/client").authenticated()
                    .requestMatchers(HttpMethod.PATCH, "/oauth2/client/{client-id}").authenticated()
                    .requestMatchers(HttpMethod.GET, "/oauth2/client/{client-id}/secret").authenticated()
                    .requestMatchers(HttpMethod.POST, "/oauth2/token").permitAll()
                    .requestMatchers(HttpMethod.GET, "/jwk").permitAll()
                    .requestMatchers(HttpMethod.GET, "/oauth2/authorize").authenticated()
                    .requestMatchers(HttpMethod.GET, "/oauth2/userinfo").permitAll()
                    .requestMatchers(HttpMethod.POST, "/oauth2/user").permitAll()
                    .requestMatchers(HttpMethod.POST, "/oauth2/user/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/oauth2/user/reissue").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/oauth2/user/update").authenticated()
                    .requestMatchers(HttpMethod.GET, "/oauth2/user/myInfo").authenticated()
                    .anyRequest().authenticated()
                    .and()

                    .apply(FilterConfig(tokenProvider, tokenResolver, exceptionHandlerFilter))
            }

        return httpSecurity.build()
    }
}
