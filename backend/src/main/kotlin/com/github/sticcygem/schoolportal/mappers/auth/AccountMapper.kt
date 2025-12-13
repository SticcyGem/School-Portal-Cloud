package com.github.sticcygem.schoolportal.mappers.auth

import com.github.sticcygem.schoolportal.dtos.auth.AccountDetails
import com.github.sticcygem.schoolportal.entities.account.Account
import com.github.sticcygem.schoolportal.entities.common.enums.AccountStatus
import com.github.sticcygem.schoolportal.entities.common.enums.AuthProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import tech.mappie.api.ObjectMappie

@Component
class AccountMapper(
    private val passwordEncoder: PasswordEncoder) : ObjectMappie<AccountDetails, Account>() {
    override fun map(from: AccountDetails) = mapping {
        to::email fromValue from.email!!
        to::passwordHash fromValue passwordEncoder.encode(from.password!!)!!
        to::status fromValue AccountStatus.ACTIVE
        to::authProvider fromValue AuthProvider.LOCAL
    }
}