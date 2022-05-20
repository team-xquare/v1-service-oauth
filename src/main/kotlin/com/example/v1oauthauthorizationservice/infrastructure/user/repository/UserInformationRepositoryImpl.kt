package com.example.v1oauthauthorizationservice.infrastructure.user.repository

import com.example.v1oauthauthorizationservice.infrastructure.configuration.properties.service.ServiceEndpointProperties
import com.example.v1oauthauthorizationservice.infrastructure.configuration.properties.service.ServiceProperties
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos.UserInformationDto
import java.util.UUID
import org.springframework.stereotype.Repository
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import org.springframework.web.util.UriComponentsBuilder

@Repository
class UserInformationRepositoryImpl(
    private val restTemplate: RestTemplate,
    private val serviceProperties: ServiceProperties
) : UserInformationRepository {

    override fun findUserById(userId: UUID): UserInformationDto {
        val userServiceUriMap = getUserServiceUriMap()
        val getUserInformationPathVariable = mutableMapOf("userId" to userId)

        val getUserInformationUri = userServiceUriMap.getBasicUserServiceUri()
            .path(userServiceUriMap.endpointMap["userinfo-by-id-endpoint"]!!)
            .buildAndExpand(getUserInformationPathVariable)

        val requestResult = try {
            restTemplate.getForEntity<UserInformationDto>(getUserInformationUri.toUri())
        } catch (httpClientException: HttpClientErrorException) {
            TODO("User Not Found Exception을 던져야 함")
        }

        return requestResult.body!!
    }

    override fun findUserByAccountId(accountId: String): UserInformationDto {
        val userServiceUriMap = getUserServiceUriMap()
        val getUserInformationPathVariable = mutableMapOf("accountId" to accountId)

        val getUserInformationUri = userServiceUriMap.getBasicUserServiceUri()
            .path(userServiceUriMap.endpointMap["userinfo-by-account-id-endpoint"]!!)
            .buildAndExpand(getUserInformationPathVariable)

        val requestResult = try {
            restTemplate.getForEntity<UserInformationDto>(getUserInformationUri.toUri())
        } catch (httpClientException: HttpClientErrorException) {
            TODO("User Not Found Exception을 던져야 함")
        }

        return requestResult.body!!
    }

    private fun ServiceEndpointProperties.getBasicUserServiceUri() =
        UriComponentsBuilder
            .fromUriString(this.baseUrl)

    private fun getUserServiceUriMap() =
        serviceProperties.serviceUrlMap["user"]!!
}