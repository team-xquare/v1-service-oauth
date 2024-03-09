package com.example.v1oauthauthorizationservice.global.common

import com.example.v1oauthauthorizationservice.infrastructure.user.entity.User
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserFacade(
    private val userRepository: UserRepository
) {
    fun getCurrentUser(): User = userRepository.findByAccountId(SecurityContextHolder.getContext().authentication.name)
        ?: throw InternalError("Internal Server Error")

    fun getCurrentUserOrNull(): User? = userRepository.findByAccountId(SecurityContextHolder.getContext().authentication.name)

}