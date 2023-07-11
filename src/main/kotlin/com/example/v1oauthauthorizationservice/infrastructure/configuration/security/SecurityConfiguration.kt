package com.example.v1oauthauthorizationservice.infrastructure.configuration.security

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

@EnableWebSecurity(debug = true)
@Configuration
class SecurityConfiguration {

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

        httpSecurity
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.POST, "/oauth2/token").permitAll()
                    .requestMatchers("/v1/users/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/jwk").permitAll()
                    .requestMatchers("/tokens").permitAll()
                    .requestMatchers(HttpMethod.GET, "/oauth2/authorize").permitAll()
                    .requestMatchers(HttpMethod.POST, "/oauth2/client").permitAll()
                    .requestMatchers(HttpMethod.GET, "/oauth2/userinfo").permitAll()
                    .anyRequest().permitAll()
            }

        return httpSecurity.build()
    }
}
