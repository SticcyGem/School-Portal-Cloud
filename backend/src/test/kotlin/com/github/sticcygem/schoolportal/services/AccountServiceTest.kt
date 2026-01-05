package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.repositories.AccountRepository
import com.github.sticcygem.schoolportal.utils.createTestAccount
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class AccountServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @InjectMocks
    private lateinit var accountService: AccountService

    @Test
    fun `should return account when email exists`() {
        val email = "test@school.edu"
        val mockAccount = createTestAccount(email = email)

        given(accountRepository.findByEmail(email)).willReturn(mockAccount)

        val result = accountService.findByEmail(email)

        assertEquals(mockAccount, result)
        verify(accountRepository).findByEmail(email)
    }

    @Test
    fun `should return null when email does not exist`() {
        val email = "unknown@school.edu"

        given(accountRepository.findByEmail(email)).willReturn(null)

        val result = accountService.findByEmail(email)

        assertNull(result)
        verify(accountRepository).findByEmail(email)
    }
}