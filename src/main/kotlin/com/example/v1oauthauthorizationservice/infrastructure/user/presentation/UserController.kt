package com.example.v1oauthauthorizationservice.infrastructure.user.presentation

import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.LoginRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.ReissueRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.SignUpRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request.UpdateProfileRequest
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.TokenResponse
import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.UserProfileResponse
import com.example.v1oauthauthorizationservice.infrastructure.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Validated
@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/myInfo")
    fun getProfile(): UserProfileResponse = userService.getProfile()

    @PutMapping("/update")
    fun updateProfile(
        @RequestBody @Valid
        req: UpdateProfileRequest
    ): UserProfileResponse = userService.updateProfile(req)

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    fun login(
        @RequestBody @Valid
        req: LoginRequest
    ): TokenResponse = userService.login(req)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(
        @RequestBody @Valid
        request: SignUpRequest
    ) {
        userService.signup(request)
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.CREATED)
    fun reissue(
        @RequestBody @Valid
        request: ReissueRequest
    ): TokenResponse = userService.reissue(request)

}