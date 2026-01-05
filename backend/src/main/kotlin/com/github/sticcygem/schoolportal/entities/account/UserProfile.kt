package com.github.sticcygem.schoolportal.entities.account

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import org.springframework.data.domain.Persistable
import java.util.UUID

@Entity
@Table(name = "user_profiles", schema = "school")
class UserProfile(
    @Id
    @Column(name = "account_id")
    @field:JsonProperty("account_id")
    var accountId: UUID? = null,

    @Column(name = "first_name", nullable = false)
    @field:JsonProperty("first_name")
    var firstName: String,

    @Column(name = "middle_name")
    @field:JsonProperty("middle_name")
    var middleName: String? = null,

    @Column(name = "last_name", nullable = false)
    @field:JsonProperty("last_name")
    var lastName: String,

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "account_id")
    @JsonIgnore
    var account: Account? = null,
) : Persistable<UUID> {
    @Transient
    private var isNewEntry: Boolean = true

    override fun getId(): UUID? = accountId
    override fun isNew(): Boolean = isNewEntry

    @PostLoad
    @PostPersist
    fun markNotNew() {
        isNewEntry = false
    }
}