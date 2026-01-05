package com.github.sticcygem.schoolportal.utils

import com.github.sticcygem.schoolportal.dtos.auth.AccountDetails
import com.github.sticcygem.schoolportal.entities.account.Account
import com.github.sticcygem.schoolportal.entities.account.Role
import com.github.sticcygem.schoolportal.entities.account.UserProfile
import com.github.sticcygem.schoolportal.entities.common.enums.AccountStatus
import java.util.UUID

// DTO Factories

fun createTestAccountDetails(
    firstName: String = "FNTest",
    lastName: String = "LNTest",
    middleName: String? = "MNTest",
    email: String = "test@school.edu",
    password: String = "password"
): AccountDetails {
    return AccountDetails(
        firstName = firstName,
        lastName = lastName,
        middleName = middleName,
        email = email,
        password = password
    )
}

// ENTITY Factories

fun createTestAccount(
    accountId: UUID = UUID.randomUUID(),
    email: String = "test@school.edu",
    passwordHash: String = "password_hashed",
    status: AccountStatus = AccountStatus.ACTIVE,
    roles: MutableSet<Role> = mutableSetOf(Role(roleName = "STUDENT"))
): Account {
    return Account(
        accountId = accountId,
        email = email,
        passwordHash = passwordHash,
        status = status,
        roles = roles
    )
}

fun createTestUserProfile(
    accountId: UUID = UUID.randomUUID(),
    firstName: String = "John",
    lastName: String = "Doe",
    middleName: String? = null
): UserProfile {
    return UserProfile(
        accountId = accountId,
        firstName = firstName,
        lastName = lastName,
        middleName = middleName
    )
}