package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2

import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(
    name = "tbl_redirect_uri",
    uniqueConstraints = [
        UniqueConstraint(
            name = "redirect_uri_unique",
            columnNames = ["id", "uri"]
        )
    ]
)
class RedirectUriEntity(
    @field:NotNull
    val uri: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registered_client_id", nullable = false)
    val registeredClient: RegisteredClientEntity,
) {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID = UUID(0, 0)
}
