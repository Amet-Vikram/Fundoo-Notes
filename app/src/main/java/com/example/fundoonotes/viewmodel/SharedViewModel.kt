package com.example.fundoonotes.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundoonotes.model.User
import com.example.fundoonotes.model.UserAuthService
import java.net.URI

private const val TAG = "SharedViewModel"

class SharedViewModel(private val userAuthService: UserAuthService): ViewModel() {

    private var _profilePicUri = MutableLiveData<Uri>()
    val profilePicUri: LiveData<Uri> = _profilePicUri

    private val _gotoLoginPageStatus = MutableLiveData<Boolean>()
    val gotoLoginPageStatus: LiveData<Boolean> = _gotoLoginPageStatus

    private val _gotoRegistrationPageStatus = MutableLiveData<Boolean>()
    val gotoRegistrationPage : LiveData<Boolean> = _gotoRegistrationPageStatus

    private val _gotoHomePageStatus = MutableLiveData<Boolean>()
    val gotoHomePageStatus: LiveData<Boolean> = _gotoHomePageStatus

    private val _userDetails = MutableLiveData<User>()
    val useDetails: LiveData<User> = _userDetails

    fun updateProfilePicture(newUri: Uri){
        _profilePicUri.value = newUri
    }

    fun setGotoLoginPageStatus(status: Boolean){
        _gotoLoginPageStatus.value = status
    }

    fun setGotoRegistrationPageStatus(status: Boolean){
        _gotoRegistrationPageStatus.value = status
    }

    fun setGotoHomePageStatus(status: Boolean){
        _gotoHomePageStatus.value = status
    }

    fun loadUserData(){

         userAuthService.loadUserData(){
             Log.i(TAG, "Mutable Data Updated")
             _userDetails.value = it
         }
    }

}