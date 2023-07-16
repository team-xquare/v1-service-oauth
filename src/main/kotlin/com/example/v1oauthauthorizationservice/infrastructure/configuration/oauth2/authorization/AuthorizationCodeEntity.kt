package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization

import java.time.Instant
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.MapsId
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

@Entity
@Table(name = "tbl_authorization_code")
class AuthorizationCodeEntity(
    @field:NotNull
    @field:Size(max = 2500)
    val codeValue: String,

    @field:Size(max = 2500)
    val state: String?,

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
