package com.github.sticcygem.schoolportal.mappers.auth

import com.github.sticcygem.schoolportal.dtos.auth.AccountDetails
import com.github.sticcygem.schoolportal.entities.account.Account
import com.github.sticcygem.schoolportal.entities.account.UserProfile
import org.springframework.stereotype.Component

@Component
class UserProfileMapper {
    fun map(from: AccountDetails, parentAccount: Account): UserProfile {
        return UserProfile(
            accountId = parentAccount.accountId,
            account = parentAccount,
            firstName = from.firstName!!,
            lastName = from.lastName!!,
            middleName = from.middleName
        )
    }
}