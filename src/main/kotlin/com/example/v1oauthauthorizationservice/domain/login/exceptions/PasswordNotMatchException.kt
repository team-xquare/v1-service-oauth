package com.example.v1oauthauthorizationservice.domain.login.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class PasswordNotMatchException(
    errorMessage: String
): BaseException(errorMessage, 401) {
    companion object {
        const val PASSWORD_NOT_MATCH_MESSAGE = "User Password Not Match"
    }
}
