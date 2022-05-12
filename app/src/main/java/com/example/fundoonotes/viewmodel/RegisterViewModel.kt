package com.example.fundoonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.AuthListener
import com.example.fundoonotes.model.UserAuthService

class RegisterViewModel(private val userAuthService: UserAuthService): ViewModel()  {

    private val _registerStatus = MutableLiveData<AuthListener>()
    val registerStatus = _registerStatus as LiveData<AuthListener>

    fun userRegister(fName: String, lName: String, email: String, password: String){
        //Passing the info to model from view
        userAuthService.userRegister(fName, lName, email, password){
            //This is Callback
            if(it.status){
                _registerStatus.value = it
            }
        }
    }
}