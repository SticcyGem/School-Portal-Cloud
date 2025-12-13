package com.github.sticcygem.schoolportal.dtos.auth

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.github.sticcygem.schoolportal.entities.common.enums.EducationLevel
import com.github.sticcygem.schoolportal.entities.common.enums.StudentType
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class AccountDetails(
    @field:JsonProperty("email")
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String? = null,

    @field:JsonProperty("password")
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String? = null,

    @field:JsonProperty("first_name")
    @field:NotBlank(message = "First name is required")
    val firstName: String? = null,

    @field:JsonProperty("last_name")
    @field:NotBlank(message = "Last name is required")
    val lastName: String? = null,

    @field:JsonProperty("middle_name")
    val middleName: String? = null
)

data class RegisterStudentRequest(
    @field:JsonUnwrapped
    @field:Valid
    val identity: AccountDetails,

    @field:JsonProperty("student_no")
    val studentNo: Long? = null,

    @field:JsonProperty("education_level")
    @field:NotNull(message = "Education level is required")
    val educationLevel: EducationLevel? = null,

    @field:JsonProperty("student_type")
    @field:NotNull(message = "Student Type is required")
    val studentType: StudentType? = null,

    @field:JsonProperty("course_code")
    @field:NotBlank(message = "Course code is required")
    val courseCode: String? = null,
)

data class RegisterAdminRequest(
    @field:JsonUnwrapped
    @field:Valid
    val identity: AccountDetails
)