package com.github.sticcygem.schoolportal.dtos.auths

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:JsonProperty("email")
    @field:NotBlank(message = "Email cannot be empty")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:JsonProperty("password")
    @field:NotBlank(message = "Password cannot be empty")
    val password: String
)