package com.example.v1oauthauthorizationservice.global.config.security.principal

import com.example.v1oauthauthorizationservice.infrastructure.user.exceptions.UserNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class AuthDetailsService(
    private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(accountId: String): UserDetails =
        AuthDetails(userRepository.findByAccountId(accountId) ?: throw UserNotFoundException("User not found"))
}