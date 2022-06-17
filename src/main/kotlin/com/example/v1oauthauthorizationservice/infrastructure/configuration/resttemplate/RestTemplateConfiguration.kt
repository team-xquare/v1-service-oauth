package com.example.v1oauthauthorizationservice.infrastructure.configuration.resttemplate

import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun restTemplate(): RestTemplate {
        val requestFactory = HttpComponentsClientHttpRequestFactory().apply {
            setReadTimeout(5000)
            setConnectTimeout(8000)
        }

        val httpClient = HttpClientBuilder.create()
            .setMaxConnTotal(50)
            .setMaxConnPerRoute(20).build()

        requestFactory.httpClient = httpClient

        return RestTemplate(requestFactory)
    }
}
