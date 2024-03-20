package com.example.v1oauthauthorizationservice.global.config.security.principal

import com.example.v1oauthauthorizationservice.infrastructure.user.entity.User
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonIgnoreProperties(ignoreUnknown = true)
class AuthDetails(
    private val user: User
) : UserDetails {

    var authorities: List<GrantedAuthority>? = null
        private set

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = ArrayList()
    override fun getPassword(): String = user.password
    override fun getUsername(): String = user.id.toString();
    override fun isAccountNonExpired(): Boolean = false
    override fun isAccountNonLocked(): Boolean = false
    override fun isCredentialsNonExpired(): Boolean = false
    override fun isEnabled(): Boolean = false

    constructor() : this(
        User(
            UUID.randomUUID(), "", "", "", "", "", ""
        )
    )
}