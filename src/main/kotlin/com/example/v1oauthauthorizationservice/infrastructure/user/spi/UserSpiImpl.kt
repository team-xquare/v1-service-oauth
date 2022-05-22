package com.example.v1oauthauthorizationservice.infrastructure.user.spi

import com.example.v1oauthauthorizationservice.domain.login.spi.UserSpi
import com.example.v1oauthauthorizationservice.domain.login.spi.dtos.UserInformationSpiDto
import com.example.v1oauthauthorizationservice.infrastructure.configuration.uuid.UuidUtils.toUUID
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.UserInformationRepository
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos.UserInformationDto
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Repository

@Repository
class UserSpiImpl(
    private val userInformationRepository: UserInformationRepository
) : UserSpi {
    @Cacheable(cacheNames = ["user"], key = "#id")
    override fun findUserById(id: String): UserInformationSpiDto {
        return userInformationRepository.findUserById(id.toUUID())
            .toDomainObject()
    }

    @Cacheable(cacheNames = ["user"], key = "#accountId")
    override fun findUserByAccountId(accountId: String): UserInformationSpiDto {
        return userInformationRepository.findUserByAccountId(accountId)
            .toDomainObject()
    }

    private fun UserInformationDto.toDomainObject() =
        UserInformationSpiDto(
            name = this.name,
            password = this.password,
            id = this.id
        )
}
