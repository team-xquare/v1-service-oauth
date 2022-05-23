package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization

import java.time.Instant
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "tbl_oidc_id_token")
class OidcIdTokenEntity(
    expiredAt: Instant,

    issuedAt: Instant = Instant.now(),

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "authorization_id", columnDefinition = "BINARY(16)", nullable = false)
    val authorization: AuthorizationEntity,

    tokenValue: String
) {

    @field:NotNull
    @field:FutureOrPresent
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    var expiredAt: Instant = expiredAt
        private set

    @field:NotNull
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    var issuedAt: Instant = issuedAt
        private set

    @field:NotNull
    @field:Size(max = 2500)
    var tokenValue: String = tokenValue
        private set

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
