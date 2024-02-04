package com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos

import java.time.LocalDate
import java.util.UUID

data class UserInformationDto(
    val id: UUID,
    val accountId: String,
    val name: String,
    val birthDay: LocalDate,
    val grade: Int,
    val classNum: Int,
    val num: Int,
    val profileFileName: String?,
    val password: String,
    val userRole: UserRole
)
