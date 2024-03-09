package com.example.v1oauthauthorizationservice.infrastructure.user.service

import com.example.v1oauthauthorizationservice.global.common.UserFacade
import com.example.v1oauthauthorizationservice.global.config.jwt.TokenProvider
import com.example.v1oauthauthorizationservice.infrastructure.configuration.exception.basic.PasswordNotMatchedException
import com.example.v1oauthauthorizationservice.infrastructure.user.entity.User
import com.example.v1oauthauthorizationservice.infrastructure.user.exceptions.UserNotFoundException
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.LoginRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.SignUpRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.ReissueRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.UpdateProfileRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.TokenResponse
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.UserProfileResponse
import com.example.v1oauthauthorizationservice.infrastructure.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserServiceImpl(
    private val userFacade: UserFacade,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenProvider: TokenProvider
) : UserService {

    override fun getProfile(): UserProfileResponse = userFacade.getCurrentUser().toResponse()

    @Transactional
    override fun updateProfile(req: UpdateProfileRequest): UserProfileResponse {
        val currentUser = userFacade.getCurrentUser()

        req.github.let {
            currentUser.updateGithub(it)
        }

        return userRepository.save(currentUser).toResponse()
    }

    @Transactional
    override fun login(req: LoginRequest): TokenResponse {
        val user = userRepository.findByAccountId(req.accountId!!)
            ?: throw UserNotFoundException("User not found")

        if (!passwordEncoder.matches(req.password, user.password)) throw PasswordNotMatchedException("Password not matched")

        return tokenProvider.receiveToken(user.accountId)
    }

    @Transactional
    override fun signup(request: SignUpRequest) {
        userRepository.save(
            User(
                studentName = request.studentName!!,
                accountId = request.accountId!!,
                password = passwordEncoder.encode(request.password!!),
                schoolGcn = request.schoolGcn!!,
                email = request.email!!,
                github = request.github!!
            )
        )
    }

    @Transactional
    override fun reissue(req: ReissueRequest): TokenResponse = tokenProvider.reissue(req.refreshToken)

    @Transactional
    override fun duplicate(accountId: String): Boolean {
        val duplicateAccountId = userRepository.findByAccountId(accountId)

        return duplicateAccountId != null
    }
}