package com.github.sticcygem.schoolportal.controllers.advice

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            mapOf(
                "status" to 400,
                "error" to "Validation Error",
                "message" to "Input validation failed",
                "fields" to errors
            )
        )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            mapOf(
                "status" to 401,
                "error" to "Unauthorized",
                "message" to (ex.message ?: "Invalid credentials")
            )
        )
    }

    @ExceptionHandler(LockedException::class)
    fun handleLocked(ex: LockedException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            mapOf(
                "status" to 403,
                "error" to "Account Locked",
                "message" to (ex.message ?: "Your account has been locked")
            )
        )
    }

    @ExceptionHandler(DisabledException::class)
    fun handleDisabled(ex: DisabledException): ResponseEntity<Map<String, Any>> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            mapOf(
                "status" to 401,
                "error" to "Account Disabled",
                "message" to (ex.message ?: "Your account is disabled")
            )
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleJsonErrors(ex: HttpMessageNotReadableException): ResponseEntity<Map<String, Any>> {
        val cause = ex.cause

        if (cause is MismatchedInputException) {
            val fieldName = cause.path.joinToString(".") { it.fieldName }

            val errorResponse = mapOf(
                "status" to 400,
                "error" to "Bad Request",
                "message" to "Invalid or missing value for field: '$fieldName'"
            )
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            mapOf(
                "status" to 400,
                "error" to "Bad Request",
                "message" to "Malformed JSON request"
            )
        )
    }
}