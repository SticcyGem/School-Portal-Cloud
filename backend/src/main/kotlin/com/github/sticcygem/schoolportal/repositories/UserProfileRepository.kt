package com.github.sticcygem.schoolportal.repositories

import com.github.sticcygem.schoolportal.entities.account.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, UUID>