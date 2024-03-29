package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.RegisteredClientEntity
import java.util.UUID
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "tbl_authorization")
class AuthorizationEntity(
    @Id
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID,

    @field:NotNull
    val authorizationGrantType: String,

    @field:NotNull
    val principalName: String,

    @field:NotNull
    val userId: UUID,

    @ManyToOne
    @JoinColumn(name = "registered_client_id", nullable = false)
    val registeredClient: RegisteredClientEntity
) {
    @OneToOne(mappedBy = "authorization", cascade = [CascadeType.REMOVE])
    val accessTokenEntity: AccessTokenEntity? = null

    @OneToOne(mappedBy = "authorization", cascade = [CascadeType.REMOVE])
    val refreshTokenEntity: RefreshTokenEntity? = null

    @OneToOne(mappedBy = "authorization", cascade = [CascadeType.REMOVE])
    val authorizationCodeEntity: AuthorizationCodeEntity? = null

    @OneToOne(mappedBy = "authorization", cascade = [CascadeType.REMOVE])
    val oidcIdTokenEntity: OidcIdTokenEntity? = null

    @OneToMany(mappedBy = "authorization", cascade = [CascadeType.REMOVE])
    val authorizationAttributeEntities: List<AuthorizationAttributeEntity>? = null
}
