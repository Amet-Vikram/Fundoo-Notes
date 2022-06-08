package com.example.fundoonotes.api

data class LoginResponse(val idToken: String, val email: String, val refreshToken: String,
                         val expiresIn: String, val localId: String) {
}