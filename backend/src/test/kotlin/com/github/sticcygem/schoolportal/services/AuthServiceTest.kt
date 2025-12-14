package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.dtos.auth.LoginRequest
import com.github.sticcygem.schoolportal.entities.account.Account
import com.github.sticcygem.schoolportal.entities.account.Role
import com.github.sticcygem.schoolportal.entities.account.UserProfile
import com.github.sticcygem.schoolportal.entities.common.enums.AccountStatus
import com.github.sticcygem.schoolportal.repositories.AccountRepository
import com.github.sticcygem.schoolportal.repositories.UserProfileRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var userProfileRepository: UserProfileRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var jwtService: JwtService

    @InjectMocks
    private lateinit var authService: AuthService

    @Test
    fun `should return AuthResponse when login is successful`() {
        val request = LoginRequest("test@school.edu", "password")
        val mockAccount = Account(
            email = "test@school.edu",
            passwordHash = "password_hashed",
            roles = mutableSetOf(Role(roleName = "STUDENT"))
        )

        given(accountRepository.findByEmail(request.email!!)).willReturn(mockAccount)
        given(passwordEncoder.matches(request.password, mockAccount.passwordHash)).willReturn(true)
        given(jwtService.generateToken(mockAccount)).willReturn("fake-jwt")
        given(userProfileRepository.findById(mockAccount.accountId)).willReturn(Optional.empty())

        val result = authService.login(request)

        assertNotNull(result)
        assertEquals("fake-jwt", result.token)
        verify(jwtService).generateToken(mockAccount)
    }

    @Test
    fun `should return UserProfile in response when profile exists`() {
        val request = LoginRequest("profile@school.edu", "password")
        val userId = UUID.randomUUID()
        val mockAccount = Account(
            accountId = userId,
            email = "profile@school.edu",
            passwordHash = "password_hashed"
        )
        val mockProfile = UserProfile(
            accountId = userId,
            firstName = "John",
            lastName = "Doe"
        )

        given(accountRepository.findByEmail(request.email!!)).willReturn(mockAccount)
        given(passwordEncoder.matches(request.password, mockAccount.passwordHash)).willReturn(true)
        given(jwtService.generateToken(mockAccount)).willReturn("token")
        given(userProfileRepository.findById(userId)).willReturn(Optional.of(mockProfile))

        val result = authService.login(request)

        assertEquals(mockProfile, result.profile)
    }

    @Test
    fun `should throw LockedException when account is LOCKED`() {
        val request = LoginRequest("locked@school.edu", "password")
        val mockAccount = Account(
            email = "locked@school.edu",
            passwordHash = "password_hashed",
            status = AccountStatus.LOCKED
        )

        given(accountRepository.findByEmail(request.email!!)).willReturn(mockAccount)

        assertThrows<LockedException> {
            authService.login(request)
        }
    }

    @Test
    fun `should throw DisabledException when account is BANNED`() {
        val request = LoginRequest("banned@school.edu", "password")
        val mockAccount = Account(
            email = "banned@school.edu",
            passwordHash = "password_hashed",
            status = AccountStatus.BANNED
        )

        given(accountRepository.findByEmail(request.email!!)).willReturn(mockAccount)

        assertThrows<DisabledException> {
            authService.login(request)
        }
    }

    @Test
    fun `should throw BadCredentialsException when password does not match`() {
        val request = LoginRequest("test@school.edu", "wrong_password")
        val mockAccount = Account(email = "test@school.edu", passwordHash = "password_hashed")

        given(accountRepository.findByEmail(request.email!!)).willReturn(mockAccount)
        given(passwordEncoder.matches(request.password, mockAccount.passwordHash)).willReturn(false)

        assertThrows<BadCredentialsException> {
            authService.login(request)
        }
    }

    @Test
    fun `should throw BadCredentialsException when email is not found`() {
        val request = LoginRequest("unknown@school.edu", "password")

        given(accountRepository.findByEmail(request.email!!)).willReturn(null)

        assertThrows<BadCredentialsException> {
            authService.login(request)
        }
    }
}