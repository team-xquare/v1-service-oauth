package com.example.v1oauthauthorizationservice.infrastructure.user.service

import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.LoginRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.ReissueRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.SignUpRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.UpdateProfileRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.TokenResponse
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.UserProfileResponse

interface UserService {

    fun getProfile(): UserProfileResponse
    fun updateProfile(req: UpdateProfileRequest): UserProfileResponse
    fun login(req: LoginRequest): TokenResponse
    fun signup(req: SignUpRequest)
    fun reissue(req: ReissueRequest): TokenResponse
    fun duplicate(accountId: String): Boolean
}