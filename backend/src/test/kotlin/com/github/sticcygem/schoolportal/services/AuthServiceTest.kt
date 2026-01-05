package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.dtos.auth.LoginRequest
import com.github.sticcygem.schoolportal.entities.common.enums.AccountStatus
import com.github.sticcygem.schoolportal.utils.createTestAccount
import com.github.sticcygem.schoolportal.utils.createTestUserProfile
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
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {
    @Mock
    private lateinit var accountService: AccountService

    @Mock
    private lateinit var userProfileService: UserProfileService

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @Mock
    private lateinit var jwtService: JwtService

    @InjectMocks
    private lateinit var authService: AuthService

    @Test
    fun `should return AuthResponse when login is successful`() {
        val request = LoginRequest("test@school.edu", "password")
        val mockAccount = createTestAccount(email = "test@school.edu")

        given(accountService.findByEmail(request.email!!)).willReturn(mockAccount)
        given(passwordEncoder.matches(request.password, mockAccount.passwordHash)).willReturn(true)
        given(jwtService.generateToken(mockAccount)).willReturn("fake-jwt")
        given(userProfileService.findByAccountId(mockAccount.accountId)).willReturn(null)

        val result = authService.login(request)

        assertNotNull(result)
        assertEquals("fake-jwt", result.token)

        verify(accountService).findByEmail(request.email)
        verify(passwordEncoder).matches(request.password, mockAccount.passwordHash)
        verify(jwtService).generateToken(mockAccount)
    }

    @Test
    fun `should return UserProfile in response when profile exists`() {
        val request = LoginRequest("profile@school.edu", "password")
        val userId = UUID.randomUUID()

        val mockAccount = createTestAccount(
            accountId = userId,
            email = "profile@school.edu"
        )
        val mockProfile = createTestUserProfile(
            accountId = userId
        )

        given(accountService.findByEmail(request.email!!)).willReturn(mockAccount)
        given(passwordEncoder.matches(request.password, mockAccount.passwordHash)).willReturn(true)
        given(jwtService.generateToken(mockAccount)).willReturn("token")
        given(userProfileService.findByAccountId(userId)).willReturn(mockProfile)

        val result = authService.login(request)

        assertEquals(mockProfile, result.profile)

        verify(userProfileService).findByAccountId(userId)
    }

    @Test
    fun `should throw LockedException when account is LOCKED`() {
        val request = LoginRequest("locked@school.edu", "password")

        val mockAccount = createTestAccount(
            email = "locked@school.edu",
            status = AccountStatus.LOCKED
        )

        given(accountService.findByEmail(request.email!!)).willReturn(mockAccount)

        assertThrows<LockedException> {
            authService.login(request)
        }

        verify(accountService).findByEmail(request.email)
    }

    @Test
    fun `should throw DisabledException when account is BANNED`() {
        val request = LoginRequest("banned@school.edu", "password")
        val mockAccount = createTestAccount(
            email = "banned@school.edu",
            status = AccountStatus.BANNED
        )

        given(accountService.findByEmail(request.email!!)).willReturn(mockAccount)

        assertThrows<DisabledException> {
            authService.login(request)
        }

        verify(accountService).findByEmail(request.email!!)
    }

    @Test
    fun `should throw BadCredentialsException when password does not match`() {
        val request = LoginRequest("test@school.edu", "wrong_password")
        val mockAccount = createTestAccount(email = "test@school.edu")

        given(accountService.findByEmail(request.email!!)).willReturn(mockAccount)
        given(passwordEncoder.matches(request.password, mockAccount.passwordHash)).willReturn(false)

        assertThrows<BadCredentialsException> {
            authService.login(request)
        }

        verify(passwordEncoder).matches(request.password, mockAccount.passwordHash)
    }

    @Test
    fun `should throw BadCredentialsException when email is not found`() {
        val request = LoginRequest("unknown@school.edu", "password")

        given(accountService.findByEmail(request.email!!)).willReturn(null)

        assertThrows<BadCredentialsException> {
            authService.login(request)
        }

        verify(accountService).findByEmail(request.email!!)
    }
}