package com.github.sticcygem.schoolportal.repositories

import com.github.sticcygem.schoolportal.entities.account.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository : JpaRepository<Account, UUID> {
    fun findByEmail(email: String): Account?
}