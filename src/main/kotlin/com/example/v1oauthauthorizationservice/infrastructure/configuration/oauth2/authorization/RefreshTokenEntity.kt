package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization

import java.time.Instant
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tbl_refresh_token")
class RefreshTokenEntity(
    expiredAt: Instant,

    issuedAt: Instant = Instant.now(),

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "authorization_id", columnDefinition = "BINARY(16)", nullable = false)
    val authorization: AuthorizationEntity,

    tokenValue: String
) {

    @field:NotNull
    @field:FutureOrPresent
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    var expiredAt: Instant = expiredAt
        protected set

    @field:NotNull
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    var issuedAt: Instant = issuedAt
        protected set

    @field:NotNull
    @field:Size(max = 2500)
    var tokenValue: String = tokenValue
        protected set

    @Id
    val id: UUID? = null

    fun updateTokenValue(tokenValue: String) {
        this.tokenValue = tokenValue
    }

    fun updateExpiration(issuedAt: Instant, expiredAt: Instant) {
        this.issuedAt = issuedAt
        this.expiredAt = expiredAt
    }
}
