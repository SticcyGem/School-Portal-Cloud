package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.entities.account.Account
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

/**
 * # JWT Service (JSON Web Token)
 *
 * This service is responsible for the **creation (signing)** and **validation** of security tokens
 * used for stateless authentication.
 *
 * ## Workflow:
 * 1. **Token Generation:** Upon successful authentication, [generateToken] is invoked to produce a signed JWT string.
 * This token encapsulates the user's identity (Account Email) and permissions (Roles).
 * 2. **Cryptographic Signing:** The token is signed using the configured [secretKey] via the HMAC-SHA256 algorithm.
 * This cryptographic signature ensures data integrity, preventing tampering with the payload (e.g., privilege escalation)
 * after issuance.
 * 3. **Validation:** For subsequent requests, [isTokenValid] verifies:
 * - **Signature Integrity:** Confirms the token was signed by this application using the secret key.
 * - **Expiration:** Checks if the current time exceeds the [expirationTime] claim.
 * - **Identity Verification:** Ensures the email in the token matches the provided user context.
 *
 * ## Key Definitions:
 * - **Claims:** Key-value pairs embedded in the token payload (e.g., `accountId`, `roles`).
 * - **HMAC-SHA256:** The symmetric signing algorithm used to secure the token.
 */
@Service
class JwtService(
    @param:Value($$"${jwt.secret}") private val secretKey: String,
    @param:Value($$"${jwt.expiration}") private val expirationTime: Long
) {

    private fun getSignInKey(): SecretKey {
        val keyBytes = secretKey.toByteArray(StandardCharsets.UTF_8)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateToken(account: Account): String {
        return generateToken(
            account.email,
            account.accountId,
            account.roles.map { it.roleName }
        )
    }

    fun generateToken(email: String, accountId: UUID, roles: List<String>): String {
        return Jwts.builder()
            .subject(email)
            .claim("accountId", accountId)
            .claim("roles", roles)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSignInKey(), Jwts.SIG.HS256)
            .compact()
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun extractEmail(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    /*fun extractRoles(token: String): List<String> {
        val claims = extractAllClaims(token)
        @Suppress("UNCHECKED_CAST")
        return claims.get("roles", List::class.java) as List<String>
    }*/

    private fun isTokenExpired(token: String): Boolean {
        return extractClaim(token, Claims::getExpiration).before(Date())
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)
        return (email == userDetails.username && !isTokenExpired(token))
    }
}