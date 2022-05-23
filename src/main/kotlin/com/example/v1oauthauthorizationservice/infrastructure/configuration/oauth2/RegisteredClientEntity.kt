package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2

import com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization.AuthorizationEntity
import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "tbl_registered_client")
class RegisteredClientEntity(
    @field:NotNull
    @field:Column(nullable = false, unique = true)
    val clientId: String,

    @field:NotNull
    val clientSecret: String,

    @field:NotNull
    val clientName: String,

    val userId: UUID,

    @OneToMany(mappedBy = "registeredClient")
    val redirectUriEntities: List<RedirectUriEntity> = mutableListOf(),

    @OneToMany(mappedBy = "registeredClient")
    val authorizationEntities: List<AuthorizationEntity> = mutableListOf()
) {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID = UUID(0, 0)
}
