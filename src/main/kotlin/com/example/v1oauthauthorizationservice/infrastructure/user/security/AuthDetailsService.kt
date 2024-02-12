package com.example.v1oauthauthorizationservice.infrastructure.user.security

import com.example.v1oauthauthorizationservice.infrastructure.user.exceptions.UserNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.UserInformationRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

@Service
class AuthDetailsService(
        private val userInformationRepository: UserInformationRepository
): UserDetailsService {
    @Throws(UserNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userInformationRepository.findUserByAccountId(username)
        user ?: throw UserNotFoundException(username)

        val authorities: Collection<GrantedAuthority> = listOf(
                SimpleGrantedAuthority("ROLE_" + user.userRole.toString())
        )

        return User(user.accountId, user.password, authorities)
    }
}