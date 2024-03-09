package com.example.v1oauthauthorizationservice.infrastructure.user.entity

import com.example.v1oauthauthorizationservice.infrastructure.user.presentation.dto.response.UserProfileResponse
import jakarta.persistence.*
import org.hibernate.annotations.DynamicUpdate
import java.util.*

@Entity(name = "tbl_user")
@DynamicUpdate
class User (
    id: UUID? = null,
    studentName: String,
    password: String,
    schoolGcn: String,
    github: String,
    accountId: String,
    email: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    var id: UUID? = id
        protected set

    @Column(name = "studentName", columnDefinition = "CHAR(4)", nullable = false)
    var studentName: String = studentName
        protected set

    @Column(name = "password", columnDefinition = "CHAR(60)", nullable = false)
    var password: String = password
        protected set

    @Column(name = "schoolGcn", columnDefinition = "CHAR(4)", nullable = false)
    var schoolGcn: String = schoolGcn
        protected set

    @Column(name = "github", columnDefinition = "VARCHAR(100)", nullable = false)
    var github: String = github
        protected set

    @Column(name = "accountId", columnDefinition = "VARCHAR(15)", nullable = false)
    var accountId: String = accountId
        protected set

    @Column(name = "email", columnDefinition = "VARCHAR(40)", nullable = false)
    var email: String = email
        protected set

    fun toResponse() = UserProfileResponse(
        this.id!!,
        this.studentName,
        this.schoolGcn,
        this.github,
        this.accountId,
        this.email
    )

    fun updateGithub(newGithub: String) {
        this.github = newGithub
    }

}