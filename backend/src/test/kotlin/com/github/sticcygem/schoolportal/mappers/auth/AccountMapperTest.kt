package com.github.sticcygem.schoolportal.mappers.auth

import com.github.sticcygem.schoolportal.entities.common.enums.AccountStatus
import com.github.sticcygem.schoolportal.entities.common.enums.AuthProvider
import com.github.sticcygem.schoolportal.utils.createTestAccountDetails
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class AccountMapperTest {

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var accountMapper: AccountMapper

    @Test
    fun `map should correctly transform AccountDetails to Account with encoded password and default status`() {
        val rawPassword = "password"
        val encodedPassword = "password_hashed"
        val email = "test@school.edu"

        val accountDetails = createTestAccountDetails(
            email = email,
            password = rawPassword
        )

        `when`(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword)

        val result = accountMapper.map(accountDetails)

        assertEquals(email, result.email)
        assertEquals(encodedPassword, result.passwordHash)
        assertEquals(AccountStatus.ACTIVE, result.status)
        assertEquals(AuthProvider.LOCAL, result.authProvider)

        verify(passwordEncoder).encode(rawPassword)
    }
}