package com.example.v1oauthauthorizationservice.infrastructure.configuration.properties

import com.example.v1oauthauthorizationservice.V1OauthAuthorizationServiceApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackageClasses = [V1OauthAuthorizationServiceApplication::class])
class ConfigurationPropertiesConfiguration