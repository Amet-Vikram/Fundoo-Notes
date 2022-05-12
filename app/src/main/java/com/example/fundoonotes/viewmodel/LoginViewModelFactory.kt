package com.example.fundoonotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.model.UserAuthService

class LoginViewModelFactory(private val userAuthService: UserAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(userAuthService) as T
    }
}