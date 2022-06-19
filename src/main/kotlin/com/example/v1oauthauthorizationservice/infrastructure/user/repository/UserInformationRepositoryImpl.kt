package com.example.v1oauthauthorizationservice.infrastructure.user.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.properties.service.ServiceEndpointProperties
import com.example.v1oauthauthorizationservice.infrastructure.configuration.properties.service.ServiceProperties
import com.example.v1oauthauthorizationservice.infrastructure.user.exceptions.UserNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos.UserInformationDto
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.util.UriComponentsBuilder
import java.util.UUID

@Repository
class UserInformationRepositoryImpl(
    private val restTemplate: RestTemplate,
    private val serviceProperties: ServiceProperties
) : UserInformationRepository {

    override fun findUserById(userId: UUID): UserInformationDto {
        val userServiceUriMap = getUserServiceUriMap()
        val getUserInformationPathVariable = mutableMapOf("userId" to userId)

        val getUserInformationUri = userServiceUriMap.getBasicUserServiceUri()
            .path("/users/id/{userId}")
            .buildAndExpand(getUserInformationPathVariable)

        val requestResult = try {
            restTemplate.getForEntity<UserInformationDto>(getUserInformationUri.toUri())
        } catch (httpClientException: HttpClientErrorException) {
            throw UserNotFoundException(UserNotFoundException.USER_NOT_FOUND_BY_ID)
        }

        return requestResult.body!!
    }

    override fun findUserByAccountId(accountId: String): UserInformationDto {
        val userServiceUriMap = getUserServiceUriMap()
        val getUserInformationPathVariable = mutableMapOf("accountId" to accountId)

        val getUserInformationUri = userServiceUriMap.getBasicUserServiceUri()
            .path("/users/account-id/{accountId}")
            .buildAndExpand(getUserInformationPathVariable)

        val requestResult = try {
            restTemplate.getForEntity<UserInformationDto>(getUserInformationUri.toUri())
        } catch (httpClientException: HttpClientErrorException) {
            throw UserNotFoundException(UserNotFoundException.USER_NOT_FOUND_BY_ACCOUNT_ID)
        }

        return requestResult.body!!
    }

    private fun ServiceEndpointProperties.getBasicUserServiceUri() =
        UriComponentsBuilder
            .fromUriString(this.baseUrl)

    private fun getUserServiceUriMap() =
        serviceProperties.serviceUrlMap["user"]!!
}
