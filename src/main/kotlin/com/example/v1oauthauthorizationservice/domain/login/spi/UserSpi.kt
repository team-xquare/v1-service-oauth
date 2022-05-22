package com.example.v1oauthauthorizationservice.domain.login.spi

import com.example.v1oauthauthorizationservice.domain.login.spi.dtos.UserInformationSpiDto

interface UserSpi {
    fun findUserById(id: String): UserInformationSpiDto
    fun findUserByAccountId(accountId: String): UserInformationSpiDto
}
