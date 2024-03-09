package com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response

import java.util.*

data class UserProfileResponse (
    val id: UUID,
    val studentName: String,
    val schoolGcn: String,
    val github: String,
    val accountId: String,
    val email: String
)