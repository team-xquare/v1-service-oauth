package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2

import org.hibernate.annotations.GenericGenerator
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotNull

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
    val id: UUID? = null
}
