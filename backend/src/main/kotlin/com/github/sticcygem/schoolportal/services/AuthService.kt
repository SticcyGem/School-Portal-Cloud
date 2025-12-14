package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.dtos.auth.AuthResponse
import com.github.sticcygem.schoolportal.dtos.auth.LoginRequest
import com.github.sticcygem.schoolportal.repositories.AccountRepository
import com.github.sticcygem.schoolportal.repositories.UserProfileRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val accountRepository: AccountRepository,
    private val userProfileRepository: UserProfileRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    fun login(request: LoginRequest): AuthResponse {
        val account = accountRepository.findByEmail(request.email!!)
            ?: throw BadCredentialsException("Invalid email or password")

        if (!account.isEnabled) {
            throw DisabledException("Account is disabled")
        }

        if (!account.isAccountNonLocked) {
            throw LockedException("Account is locked")
        }

        if (!passwordEncoder.matches(request.password!!, account.passwordHash)) {
            throw BadCredentialsException("Invalid email or password")
        }

        val token = jwtService.generateToken(account)
        val profile = userProfileRepository.findById(account.accountId).orElse(null)

        return AuthResponse(
            token = token,
            roles = account.roles.map { it.roleName },
            accountId = account.accountId,
            profile = profile
        )
    }
}