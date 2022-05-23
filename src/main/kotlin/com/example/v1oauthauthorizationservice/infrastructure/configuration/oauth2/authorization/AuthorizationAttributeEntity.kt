package com.example.v1oauthauthorizationservice.infrastructure.configuration.oauth2.authorization

import com.fasterxml.jackson.databind.JsonNode
import com.vladmihalcea.hibernate.type.json.JsonStringType
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import java.util.UUID
import javax.persistence.Cacheable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@TypeDef(name = "json", typeClass = JsonStringType::class)
@Table(name = "tbl_authorization_attribute")
@Cacheable
class AuthorizationAttributeEntity(
    @field:NotNull
    val attributeKey: String,

    @field:NotNull
    @field:Type(type = "json")
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
