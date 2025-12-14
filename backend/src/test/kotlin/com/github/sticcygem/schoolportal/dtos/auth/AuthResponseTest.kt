package com.github.sticcygem.schoolportal.dtos.auth

import com.github.sticcygem.schoolportal.entities.account.UserProfile
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import java.util.UUID

@JsonTest
class AuthResponseTest {

    @Autowired
    private lateinit var json: JacksonTester<AuthResponse>

    @Test
    fun `should serialize object to correct JSON structure`() {
        val uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")

        val profile = UserProfile(
            accountId = uuid,
            firstName = "Emmanuel",
            middleName = "A",
            lastName = "Perez"
        )

        val response = AuthResponse(
            token = "fake-jwt-token",
            roles = listOf("STUDENT", "ADMIN"),
            accountId = uuid,
            profile = profile
        )

        val result = json.write(response)

        assertThat(result).extractingJsonPathStringValue("$.token")
            .isEqualTo("fake-jwt-token")

        assertThat(result).extractingJsonPathArrayValue<Any>("$.roles")
            .containsExactly("STUDENT", "ADMIN")

        assertThat(result).extractingJsonPathStringValue("$.account_id")
            .isEqualTo("123e4567-e89b-12d3-a456-426614174000")

        assertThat(result).extractingJsonPathStringValue("$.profile.first_name")
            .isEqualTo("Emmanuel")

        assertThat(result).extractingJsonPathStringValue("$.profile.middle_name")
            .isEqualTo("A")

        assertThat(result).extractingJsonPathStringValue("$.profile.last_name")
            .isEqualTo("Perez")
    }

    @Test
    fun `should handle null profile during serialization`() {
        val response = AuthResponse(
            token = "token",
            roles = listOf("PROFESSOR"),
            accountId = UUID.randomUUID(),
            profile = null
        )

        val result = json.write(response)

        assertThat(result).hasJsonPath("$.profile")
        assertThat(result).extractingJsonPathValue("$.profile").isNull()
    }
}