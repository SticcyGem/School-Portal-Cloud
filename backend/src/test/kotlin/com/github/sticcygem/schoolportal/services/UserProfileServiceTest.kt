package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.repositories.UserProfileRepository
import com.github.sticcygem.schoolportal.utils.createTestUserProfile
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class UserProfileServiceTest {

    @Mock
    private lateinit var userProfileRepository: UserProfileRepository

    @InjectMocks
    private lateinit var userProfileService: UserProfileService

    @Test
    fun `should return profile when account id exists`() {
        val id = UUID.randomUUID()
        val mockProfile = createTestUserProfile(accountId = id)

        given(userProfileRepository.findById(id)).willReturn(Optional.of(mockProfile))

        val result = userProfileService.findByAccountId(id)

        assertEquals(mockProfile, result)

        verify(userProfileRepository).findById(id)
    }

    @Test
    fun `should return null when account id does not exist`() {
        val id = UUID.randomUUID()

        given(userProfileRepository.findById(id)).willReturn(Optional.empty())

        val result = userProfileService.findByAccountId(id)

        assertNull(result)

        verify(userProfileRepository).findById(id)
    }
}