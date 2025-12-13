package com.github.sticcygem.schoolportal.controllers.auth

import com.github.sticcygem.schoolportal.dtos.auth.AuthResponse
import com.github.sticcygem.schoolportal.dtos.auth.LoginRequest
import com.github.sticcygem.schoolportal.services.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody
        request: LoginRequest
    ): ResponseEntity<AuthResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }
}