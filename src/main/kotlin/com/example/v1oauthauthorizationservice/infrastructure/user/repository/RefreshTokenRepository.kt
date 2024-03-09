package com.example.v1oauthauthorizationservice.infrastructure.user.repository

import com.example.v1oauthauthorizationservice.infrastructure.user.entity.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: CrudRepository<RefreshToken, String>
