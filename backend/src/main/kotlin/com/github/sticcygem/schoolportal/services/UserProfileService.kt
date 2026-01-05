package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.entities.account.UserProfile
import com.github.sticcygem.schoolportal.repositories.UserProfileRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserProfileService(private val repository: UserProfileRepository) {
    fun findByAccountId(id: UUID): UserProfile? {
        return repository.findById(id).orElse(null)
    }
}