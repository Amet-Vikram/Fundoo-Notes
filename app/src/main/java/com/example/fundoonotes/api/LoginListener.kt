package com.example.fundoonotes.api

interface LoginListener {

    fun getLoginDone (response: LoginResponse?, status: Boolean, message: String)
}