package com.example.v1oauthauthorizationservice.domain.login.api

import com.example.v1oauthauthorizationservice.domain.login.api.dtos.TokensApiDto
import com.example.v1oauthauthorizationservice.domain.login.api.dtos.DomainUserLoginDto

interface LoginApi {
    fun userLoginWithCredentials(domainUserLoginDto: DomainUserLoginDto): TokensApiDto
}
