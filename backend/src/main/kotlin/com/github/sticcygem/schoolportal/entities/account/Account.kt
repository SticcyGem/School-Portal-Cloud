package com.github.sticcygem.schoolportal.entities.account

import com.github.sticcygem.schoolportal.entities.common.enums.AccountStatus
import com.github.sticcygem.schoolportal.entities.common.enums.AuthProvider
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "accounts", schema = "school")
class Account(
    @Id
    @Column(name = "account_id")
    var accountId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", columnDefinition = "school.account_status")
    var status: AccountStatus = AccountStatus.ACTIVE,

    @Column(name = "email", unique = true, nullable = false)
    var email: String,

    @Column(name = "password_hash")
    var passwordHash: String,

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "auth_provider", columnDefinition = "school.auth_provider_enum")
    var authProvider: AuthProvider = AuthProvider.LOCAL,

    @Column(name = "lock_time", nullable = false)
    var lockTime: LocalDateTime? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "account_roles",
        schema = "school",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "role_no")]
    )
    var roles: MutableSet<Role> = mutableSetOf(),

    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY)
    var userProfile: UserProfile? = null

) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority("ROLE_${it.roleName}") }.toMutableList()
    }

    override fun getPassword(): String = passwordHash

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean {
        return status != AccountStatus.LOCKED
    }

    override fun isEnabled(): Boolean {
        return status == AccountStatus.ACTIVE || status == AccountStatus.LOCKED
    }
}