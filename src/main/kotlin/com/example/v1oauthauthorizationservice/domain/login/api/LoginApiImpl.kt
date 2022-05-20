package com.example.v1oauthauthorizationservice.domain.login.api

import com.example.v1oauthauthorizationservice.domain.login.api.dtos.DomainUserLoginDto
import com.example.v1oauthauthorizationservice.domain.login.api.dtos.TokensApiDto
import com.example.v1oauthauthorizationservice.domain.login.exceptions.PasswordNotMatchException
import com.example.v1oauthauthorizationservice.domain.login.spi.JwtTokenGeneratorSpi
import com.example.v1oauthauthorizationservice.domain.login.spi.PasswordMatcherSpi
import com.example.v1oauthauthorizationservice.domain.login.spi.UserSpi
import com.example.v1oauthauthorizationservice.domain.login.spi.dtos.TokensSpiDto
import com.example.v1oauthauthorizationservice.domain.login.spi.dtos.UserInformationSpiDto
import org.springframework.stereotype.Service

@Service
class LoginApiImpl(
    private val userSpi: UserSpi,
    private val passwordMatcherSpi: PasswordMatcherSpi,
    private val jwtTokenGeneratorSpi: JwtTokenGeneratorSpi
) : LoginApi {
    override fun userLoginWithCredentials(domainUserLoginDto: DomainUserLoginDto): TokensApiDto {
        val userInformation = userSpi.findUserByAccountId(domainUserLoginDto.accountId)
        checkPasswordMatches(domainUserLoginDto, userInformation)
        val tokensSpiDto = jwtTokenGeneratorSpi.generateJwtTokenString(userInformation.id.toString())

        return tokensSpiDto.toTokenApiDto()
    }

    private fun checkPasswordMatches(
        domainUserLoginDto: DomainUserLoginDto,
        userInformation: UserInformationSpiDto
    ) {
        val rawPassword = domainUserLoginDto.password
        val encodedPassword = userInformation.password
        val isPasswordMatch = passwordMatcherSpi.isRawPasswordAndEncodedPasswordMatches(rawPassword, encodedPassword)

        if (!isPasswordMatch) {
            throw PasswordNotMatchException(PasswordNotMatchException.PASSWORD_NOT_MATCH_MESSAGE)
        }
    }

    private fun TokensSpiDto.toTokenApiDto() =
        TokensApiDto(
            accessToken = this.accessToken,
            refreshToken = this.refreshToken
        )
}
