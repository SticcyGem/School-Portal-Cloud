package com.github.sticcygem.schoolportal.services

import com.github.sticcygem.schoolportal.utils.createTestAccount
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.charset.StandardCharsets
import java.util.Date
import java.util.UUID

class JwtServiceTest {

    private lateinit var jwtService: JwtService
    private val secretKey = "4d506f05b77b66837fdb20360c101c3e89ce7e2a0454523a7575f9d4b8053ad4"
    private val expiration = 300000

    @BeforeEach
    fun setUp() {
        jwtService = JwtService(secretKey, expiration)
    }

    @Test
    fun `should generate valid token and extract username`() {
        val account = createTestAccount()
        val token = jwtService.generateToken(account)

        assertTrue(jwtService.isTokenValid(token, account))
        assertEquals(account.email, jwtService.extractEmail(token))
    }

    @Test
    fun `should fail validation if username does not match`() {
        val account = createTestAccount(email = "test@school.edu")
        val otherAccount = createTestAccount(email = "other@school.edu")

        val token = jwtService.generateToken(account)

        assertFalse(jwtService.isTokenValid(token, otherAccount))
    }

    @Test
    fun `should fail validation if token is expired`() {
        val account = createTestAccount()

        val pastDate = Date(System.currentTimeMillis() - 1000)
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray(StandardCharsets.UTF_8))

        val expiredToken = Jwts.builder()
            .subject(account.email)
            .expiration(pastDate)
            .signWith(key, Jwts.SIG.HS256)
            .compact()

        assertThrows(io.jsonwebtoken.ExpiredJwtException::class.java) {
            jwtService.isTokenValid(expiredToken, account)
        }
    }

    @Test
    fun `should throw exception for malformed token`() {
        val account = createTestAccount()
        val garbageToken = "garbage"

        assertThrows(Exception::class.java) {
            jwtService.isTokenValid(garbageToken, account)
        }
    }

    @Test
    fun `should extract custom claims`() {
        val uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
        val account = createTestAccount(accountId = uuid)

        val token = jwtService.generateToken(account)

        val extractedId = jwtService.extractClaim(token) { claims ->
            claims["accountId", String::class.java]
        }

        assertEquals(uuid.toString(), extractedId)
    }
}