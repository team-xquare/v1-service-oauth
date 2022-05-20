package com.example.v1oauthauthorizationservice.infrastructure.user.router

import com.example.v1oauthauthorizationservice.domain.login.api.LoginApi
import com.example.v1oauthauthorizationservice.domain.login.api.dtos.TokensApiDto
import com.example.v1oauthauthorizationservice.domain.login.request.DomainUserLoginRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.router.dtos.UserLoginRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController(
    private val loginApi: LoginApi
) {
    @PostMapping("/login")
    fun login(@RequestBody userLoginRequest: UserLoginRequest): TokensApiDto {
        val domainUserLoginRequest = DomainUserLoginRequest(
            accountId = userLoginRequest.accountId,
            password = userLoginRequest.password
        )

        return loginApi.userLoginWithCredentials(domainUserLoginRequest)
    }

    @GetMapping("/login")
    fun test() {

    }
}