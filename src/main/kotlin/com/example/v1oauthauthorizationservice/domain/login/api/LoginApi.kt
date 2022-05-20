package com.example.v1oauthauthorizationservice.domain.login.api

import com.example.v1oauthauthorizationservice.domain.login.api.dtos.DomainUserLoginDto
import com.example.v1oauthauthorizationservice.domain.login.api.dtos.TokensApiDto

interface LoginApi {
    fun userLoginWithCredentials(domainUserLoginDto: DomainUserLoginDto): TokensApiDto
}
