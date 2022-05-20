package com.example.v1oauthauthorizationservice.infrastructure.user.exceptions

import com.example.v1oauthauthorizationservice.domain.exception.BaseException

class UserNotFoundException(
    errorMessage: String
): BaseException(errorMessage, 404) {
    companion object {
        const val USER_NOT_FOUND_BY_ACCOUNT_ID = "User Not Found For This Account Id"
        const val USER_NOT_FOUND_BY_ID = "User Not Found For This Id"
    }
}
