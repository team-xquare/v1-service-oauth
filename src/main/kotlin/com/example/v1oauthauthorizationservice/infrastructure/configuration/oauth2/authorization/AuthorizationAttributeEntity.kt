package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization

import com.fasterxml.jackson.databind.JsonNode
import com.vladmihalcea.hibernate.type.json.JsonType
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import java.util.UUID
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "tbl_authorization_attribute")
class AuthorizationAttributeEntity(
    @field:NotNull
    val attributeKey: String,

    @field:NotNull
    @Type(JsonType::class)
    @field:Column(columnDefinition = "json", nullable = false)
    val attributeValue: JsonNode,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authorization_id", nullable = false)
    val authorization: AuthorizationEntity
) {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID = UUID(0, 0)
}
