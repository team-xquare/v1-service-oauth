package com.example.v1oauthauthorizationservice.infrastructure.configuration.objectmapper

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.jackson2.SecurityJackson2Modules
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module

@Configuration
class ObjectMapperConfiguration {
    companion object {
        const val BASIC_BEAN_NAME = "basic"
        const val AUTHORIZATION_SERVER_BEAN_NAME = "authorizationServer"
    }

    @Primary
    @Bean(name = [BASIC_BEAN_NAME])
    fun customObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        objectMapper.propertyNamingStrategy = SNAKE_CASE
        objectMapper.registerModule(KotlinModule.Builder().build())
        return objectMapper
    }

    @Bean(name = [AUTHORIZATION_SERVER_BEAN_NAME])
    fun customObjectMapperForAuthorizationServer(): ObjectMapper {
        val objectMapper = ObjectMapper()
        val classLoader = JdbcOAuth2AuthorizationService::class.java.classLoader
        val securityModules = SecurityJackson2Modules.getModules(classLoader)

        objectMapper.registerModules(securityModules)
        objectMapper.registerModule(OAuth2AuthorizationServerJackson2Module())
        objectMapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        return objectMapper
    }
}
