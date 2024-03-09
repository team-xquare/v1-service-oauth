package com.example.v1oauthauthorizationservice.infrastructure.user.repository

import com.example.v1oauthauthorizationservice.infrastructure.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, UUID?> {

    fun findByAccountId(accountId: String): User?
    fun findUserById(userId: UUID): User?
}