package com.github.sticcygem.schoolportal.dtos.auth

import com.github.sticcygem.schoolportal.entities.common.enums.EducationLevel
import com.github.sticcygem.schoolportal.entities.common.enums.StudentType
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RegisterRequestTest {

    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    private fun <T> validateRequest(request: T) = validator.validate(request)

    private fun createAccountDetails(
        email: String? = "test@school.edu",
        password: String? = "securePassword123",
        firstName: String? = "John",
        lastName: String? = "Doe",
        middleName: String? = null
    ) = AccountDetails(email, password, firstName, lastName, middleName)

    @Test
    fun `should pass validation with valid student request`() {
        val request = RegisterStudentRequest(
            identity = createAccountDetails(),
            educationLevel = EducationLevel.UNDERGRADUATE,
            studentType = StudentType.REGULAR,
            courseCode = "CS-101"
        )
        assertTrue(validateRequest(request).isEmpty(), "Expected no validation errors")
    }

    @Test
    fun `should pass validation with valid admin request`() {
        val request = RegisterAdminRequest(
            identity = createAccountDetails()
        )
        assertTrue(validateRequest(request).isEmpty(), "Expected no validation errors")
    }

    @Test
    fun `should fail validation when email format is invalid`() {
        val request = RegisterAdminRequest(identity = createAccountDetails(email = "not-an-email"))
        val violations = validateRequest(request)

        assertFalse(violations.isEmpty())
        assertTrue(violations.any { it.message == "Invalid email format" })
    }

    @Test
    fun `should fail validation when email is null`() {
        val request = RegisterAdminRequest(identity = createAccountDetails(email = null))
        val violations = validateRequest(request)

        assertTrue(violations.any { it.message == "Email is required" })
    }

    @Test
    fun `should fail validation when password is too short`() {
        val request = RegisterAdminRequest(identity = createAccountDetails(password = "short"))
        val violations = validateRequest(request)

        assertTrue(violations.any { it.message == "Password must be at least 8 characters" })
    }

    @Test
    fun `should fail validation when names are missing`() {
        val request = RegisterAdminRequest(
            identity = createAccountDetails(firstName = null, lastName = null)
        )
        val violations = validateRequest(request)

        assertEquals(2, violations.size)
        assertTrue(violations.any { it.message == "First name is required" })
        assertTrue(violations.any { it.message == "Last name is required" })
    }

    @Test
    fun `should fail validation when education level is missing`() {
        val request = RegisterStudentRequest(
            identity = createAccountDetails(),
            studentType = StudentType.REGULAR,
            courseCode = "CS-101",
            educationLevel = null
        )
        val violations = validateRequest(request)

        assertTrue(violations.any { it.message == "Education level is required" })
    }

    @Test
    fun `should fail validation when student type is missing`() {
        val request = RegisterStudentRequest(
            identity = createAccountDetails(),
            educationLevel = EducationLevel.UNDERGRADUATE,
            courseCode = "CS-101",
            studentType = null
        )
        val violations = validateRequest(request)

        assertTrue(violations.any { it.message == "Student Type is required" })
    }

    @Test
    fun `should fail validation when course code is missing`() {
        val request = RegisterStudentRequest(
            identity = createAccountDetails(),
            educationLevel = EducationLevel.UNDERGRADUATE,
            studentType = StudentType.REGULAR,
            courseCode = null
        )
        val violations = validateRequest(request)

        assertTrue(violations.any { it.message == "Course code is required" })
    }
}