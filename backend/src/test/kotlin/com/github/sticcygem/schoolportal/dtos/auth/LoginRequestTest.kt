package com.github.sticcygem.schoolportal.dtos.auth

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class LoginRequestTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    private fun <T> validate(request: T) = validator.validate(request)

    @Test
    fun `valid credentials pass validation`() {
        val request = LoginRequest(
            email = "test@school.edu",
            password = "securePassword123"
        )
        assertTrue(validate(request).isEmpty())
    }

    @Test
    fun `invalid EMAIL format fails`() {
        val request = LoginRequest(
            email = "not-an-email",
            password = "password123"
        )
        val violations = validate(request)
        assertTrue(violations.any { it.message == "Invalid email format" })
    }

    @Test
    fun `missing EMAIL fails`() {
        val request = LoginRequest(
            email = null,
            password = "password123"
        )
        val violations = validate(request)
        assertTrue(violations.any { it.message == "Email cannot be empty" })
    }

    @Test
    fun `missing PASSWORD fails`() {
        val request = LoginRequest(
            email = "test@school.edu",
            password = null
        )
        val violations = validate(request)
        assertTrue(violations.any { it.message == "Password cannot be empty" })
    }

    @Test
    fun `blank fields fail`() {
        val request = LoginRequest(
            email = "",
            password = ""
        )
        val violations = validate(request)
        val messages = violations.map { it.message }
        assertTrue(messages.contains("Email cannot be empty"))
        assertTrue(messages.contains("Password cannot be empty"))
    }
}