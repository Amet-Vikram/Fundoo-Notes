package com.example.fundoonotes.api

data class LoginRequest(val email: String, val password: String, val returnSecureToken: Boolean) {
}