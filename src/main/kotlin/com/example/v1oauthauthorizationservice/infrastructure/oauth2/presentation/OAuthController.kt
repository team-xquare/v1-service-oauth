package com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation

import com.example.v1oauthauthorizationservice.domain.oauth.api.OAuthApi
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request.RegisterClientRequest
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.request.UpdateClientRequest
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.RegenerateSecretResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.RegisterClientResponse
import com.example.v1oauthauthorizationservice.infrastructure.oauth2.presentation.dto.response.UpdateClientResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth2")
class OAuthController(
    private val OAuthApi: OAuthApi
) {

    @PostMapping("/client")
    fun registerClient(@RequestBody request: RegisterClientRequest): RegisterClientResponse {
        return OAuthApi.registerClient(request)
    }

    @PatchMapping("/client/{client-id}")
    fun updateClient(@PathVariable("client-id") clientId: String, @RequestBody request: UpdateClientRequest): UpdateClientResponse {
        return OAuthApi.updateClient(clientId, request)
    }

    @GetMapping("/client/{client-id}/secret")
    fun regenerateSecret(@PathVariable("client-id") clientId: String): RegenerateSecretResponse {
        return OAuthApi.regenerateSecret(clientId)
    }
}
