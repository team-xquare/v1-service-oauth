package com.example.v1oauthauthorizationservice.infrastructure.feign

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import feign.Response
import feign.Util
import feign.codec.Decoder
import feign.codec.ErrorDecoder
import java.lang.reflect.Type
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["com.example.v1oauthauthorizationservice.infrastructure.feign"])
@ImportAutoConfiguration(FeignAutoConfiguration::class)
@Configuration
class FeignConfig(
    private val objectMapper: ObjectMapper
) {
    @Bean
    @ConditionalOnMissingBean(value = [ErrorDecoder::class])
    fun commonFeignErrorDecoder(): FeignClientErrorDecoder {
        return FeignClientErrorDecoder()
    }

    @Bean
    fun feignDecoder() =
        Decoder { response: Response, type: Type ->
            val bodyStr = Util.toString(
                response.body().asReader(Util.UTF_8)
            )
            val javaType = TypeFactory.defaultInstance().constructType(type)
            objectMapper.readValue(bodyStr, javaType)
        }
}
