package com.github.sticcygem.schoolportal.mappers.auths

import com.github.sticcygem.schoolportal.dtos.auths.LoginRequest
import com.github.sticcygem.schoolportal.entities.accounts.Account
import tech.mappie.api.ObjectMappie

object LoginToAccountMapper : ObjectMappie<LoginRequest, Account>() {
    override fun map(from: LoginRequest) = mapping {
        to::passwordHash fromValue from.password
        // to::passwordHash fromValue passwordEncoder.encode(from.password)
    }
}