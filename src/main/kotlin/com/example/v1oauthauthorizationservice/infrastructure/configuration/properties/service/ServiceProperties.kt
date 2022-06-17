package com.example.v1oauthauthorizationservice.infrastructure.configuration.properties.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty

@ConfigurationProperties(prefix = "service")
@ConstructorBinding
class ServiceProperties(
    @NestedConfigurationProperty
    val serviceUrlMap: HashMap<String, ServiceEndpointProperties>
)

@ConstructorBinding
class ServiceEndpointProperties(
    val baseUrl: String
)
