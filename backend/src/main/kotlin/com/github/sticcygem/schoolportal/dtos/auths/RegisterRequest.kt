package com.github.sticcygem.schoolportal.dtos.auths

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.sticcygem.schoolportal.entities.commons.enums.EducationLevel
import com.github.sticcygem.schoolportal.entities.commons.enums.EmployeeType
import com.github.sticcygem.schoolportal.entities.commons.enums.StudentType
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class RegisterAdminRequest(
    @field:JsonProperty("email")
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:JsonProperty("password")
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String,

    @field:JsonProperty("first_name")
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:JsonProperty("middle_name")
    val middleName: String? = null,

    @field:JsonProperty("last_name")
    @field:NotBlank(message = "Last name is required")
    val lastName: String
)

data class RegisterStudentRequest(
    @field:JsonProperty("email")
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:JsonProperty("password")
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String,

    @field:JsonProperty("first_name")
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:JsonProperty("middle_name")
    val middleName: String? = null,

    @field:JsonProperty("last_name")
    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:JsonProperty("student_no")
    val studentNo: Long? = null,

    @field:JsonProperty("education_level")
    @field:NotNull(message = "Education level is required")
    var educationLevel: EducationLevel,

    @field:JsonProperty("student_type")
    @field:NotNull(message = "Student type is required")
    var studentType: StudentType,

    @field:JsonProperty("course_code")
    @field:NotBlank(message = "Course code is required")
    val courseCode: String,

    @field:JsonProperty("block_no")
    val blockNo: Long? = null
)

data class RegisterProfessorRequest(
    @field:JsonProperty("email")
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,

    @field:JsonProperty("password")
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String,

    @field:JsonProperty("first_name")
    @field:NotBlank(message = "First name is required")
    val firstName: String,

    @field:JsonProperty("middle_name")
    val middleName: String? = null,

    @field:JsonProperty("last_name")
    @field:NotBlank(message = "Last name is required")
    val lastName: String,

    @field:JsonProperty("professor_id")
    val professorId: String? = null,

    @field:JsonProperty("employee_type")
    @field:NotNull(message = "Employee type is required")
    var employeeType: EmployeeType
)