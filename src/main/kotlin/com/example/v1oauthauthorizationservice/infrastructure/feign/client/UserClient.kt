package com.example.v1oauthauthorizationservice.infrastructure.feign.client

import com.example.v1oauthauthorizationservice.infrastructure.user.repository.dtos.UserInformationDto
import java.util.UUID
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "UserClient", url = "http://localhost:8080")//"\${service.scheme}://\${service.user.host}")
interface UserClient {

    @GetMapping("/users/id/{userId}")
    fun getUserByUserId(@PathVariable("userId") userId: UUID): UserInformationDto

    @GetMapping("/account-id/{accountId}")
    fun getUserByAccountId(@PathVariable("accountId") accountId: String): UserInformationDto
}