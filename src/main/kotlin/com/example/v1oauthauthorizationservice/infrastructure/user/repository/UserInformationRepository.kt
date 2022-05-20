package com.example.v1oauthauthorizationservice.infrastructure.user.repository

import com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos.UserInformationDto
import java.util.UUID

interface UserInformationRepository {
    fun findUserById(userId: UUID): UserInformationDto
    fun findUserByAccountId(accountId: String): UserInformationDto
}
