package com.example.v1oauthauthorizationservice.infrastructure.feign

import feign.codec.ErrorDecoder
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@EnableFeignClients(basePackages = ["com.example.v1oauthauthorizationservice.infrastructure.feign"])
@ImportAutoConfiguration(FeignAutoConfiguration::class)
@Configuration
class FeignConfig {
    @Bean
    @ConditionalOnMissingBean(value = [ErrorDecoder::class])
    fun commonFeignErrorDecoder(): FeignClientErrorDecoder {
        return FeignClientErrorDecoder()
    }
}