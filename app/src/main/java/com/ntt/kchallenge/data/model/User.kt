package com.ntt.kchallenge.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    val username: String,
    val password: String
)
