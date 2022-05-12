package com.example.fundoonotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fundoonotes.model.UserAuthService

class RegisterViewModelFactory(private val userAuthService: UserAuthService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(userAuthService) as T
    }
}