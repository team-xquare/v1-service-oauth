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
    @field:NotNull
    @field:Size(max = 2500)
    val tokenValue: String,

    @field:NotNull
    @field:FutureOrPresent
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    val expiredAt: Instant,

    @field:NotNull
    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    val issuedAt: Instant = Instant.now(),

    @MapsId
    @OneToOne(optional = false)
    @JoinColumn(name = "authorization_id", columnDefinition = "BINARY(16)", nullable = false)
    val authorization: AuthorizationEntity
) {

    @Id
    val id: UUID? = null
}
