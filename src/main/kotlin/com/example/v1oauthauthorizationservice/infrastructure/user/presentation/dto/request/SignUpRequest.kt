package com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @field:NotBlank(message = "null이 될 수 없습니다.")
    @field:Size(min = 2, max = 4, message = "이름은 2자 ~ 4자입니다.")
    val studentName: String?,

    @field:NotBlank(message = "null이 될 수 없습니다.")
    @field:Pattern(
        regexp = "^https://github\\.com/[A-z0-9_-]+(/)?([A-z0-9_-]+(/)?)?$",
        message = "올바른 Github 주소 형식이 아닙니다."
    )
    val github: String?,


    @field:NotBlank(message = "null이 될 수 없습니다.")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\\\\|,.<>\\/?]).{8,15}\$",
        message = "8자 이상 15자 이하이며, 최소 하나의 숫자, 특수문자, 영문자를 포함해야 합니다."
    )
    val password: String?,

    @field:NotNull(message = "null이 될 수 없습니다.")
    @field:Size(min = 4, max = 4, message = "학번은 4자리입니다.")
    @field:Pattern(
        regexp = "^[123].*",
        message = "학번의 1~3으로 시작해야 합니다."
    )
    val schoolGcn: String?,

    @field:NotNull(message = "null이 될 수 없습니다.")
    @field:Pattern(
        regexp = ".*@dsm.hs.kr.*",
        message = "올바른 이메일 형식이 아닙니다."
    )
    val email: String?,

    @field:NotNull(message = "null이 될 수 없습니다.")
    @field:Size(min = 5, max = 15, message = "아이디는 최소 5자, 최대 15자 입니다.")
    val accountId: String?
)