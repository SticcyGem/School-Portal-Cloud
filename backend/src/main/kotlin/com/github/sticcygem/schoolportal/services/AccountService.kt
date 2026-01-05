package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.entities.account.Account
import com.github.sticcygem.schoolportal.repositories.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    fun findByEmail(email: String): Account? {
        return accountRepository.findByEmail(email)
    }
}