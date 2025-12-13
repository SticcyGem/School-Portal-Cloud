package com.github.sticcygem.schoolportal.controllers.auth

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.sticcygem.schoolportal.dtos.auth.AuthResponse
import com.github.sticcygem.schoolportal.dtos.auth.LoginRequest
import com.github.sticcygem.schoolportal.services.AuthService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.UUID

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var authService: AuthService

    private val mapper = jacksonObjectMapper()

    @Test
    fun `should return 200 OK and Token on successful login`() {
        val request = LoginRequest("test@school.edu", "password123")
        val fakeResponse = AuthResponse("fake-jwt-token", listOf("STUDENT"), UUID.randomUUID(), null)

        given(authService.login(request)).willReturn(fakeResponse)

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").value("fake-jwt-token"))
    }

    @Test
    fun `should return 400 Bad Request on invalid input`() {
        val invalidRequest = LoginRequest("", "")

        mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Validation Error"))
    }
}