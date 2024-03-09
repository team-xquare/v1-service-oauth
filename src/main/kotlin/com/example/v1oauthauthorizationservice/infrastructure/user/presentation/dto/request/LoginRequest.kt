package com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotNull(message = "null이 될 수 없습니다.")
    @field:Size(min = 5, max = 15, message = "아이디는 최소 5자, 최대 15자 입니다.")
    val accountId: String?,

    @field:NotNull(message = "null이 될 수 없습니다.")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\\\\|,.<>\\/?]).{8,15}\$",
        message = "8자 이상 15자 이하이며, 최소 하나의 숫자, 특수문자, 영문자를 포함해야 합니다."
    )
    val password: String?
)