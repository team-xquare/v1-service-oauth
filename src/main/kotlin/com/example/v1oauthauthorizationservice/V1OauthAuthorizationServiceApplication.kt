package com.example.v1oauthauthorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.expression.common.ExpressionUtils.toByte

@SpringBootApplication
class V1OauthAuthorizationServiceApplication

fun main(args: Array<String>) {
    runApplication<V1OauthAuthorizationServiceApplication>(*args)
}
