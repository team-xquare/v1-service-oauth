package com.example.v1oauthauthorizationservice.infrastructure.user.repository

import com.example.v1oauthauthorizationservice.infrastructure.feign.client.UserClient
import com.example.v1oauthauthorizationservice.infrastructure.feign.exception.FeignNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.user.exceptions.UserNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos.UserInformationDto
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class UserInformationRepositoryImpl(
    private val userClient: UserClient
) : UserInformationRepository {

    override fun findUserById(userId: UUID): UserInformationDto =
        try {
            userClient.getUserByUserId(userId)
        } catch (e: FeignNotFoundException) {
            throw UserNotFoundException(UserNotFoundException.USER_NOT_FOUND_BY_ID)
        }

    override fun findUserByAccountId(accountId: String) =
        try {
            userClient.getUserByAccountId(accountId)
        } catch (e: FeignNotFoundException) {
            throw UserNotFoundException(UserNotFoundException.USER_NOT_FOUND_BY_ACCOUNT_ID)
        }
}
