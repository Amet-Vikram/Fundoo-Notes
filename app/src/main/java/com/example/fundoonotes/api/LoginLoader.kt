package com.example.fundoonotes.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "LoginLoader"
class LoginLoader {

    fun getLoginCompleted(listener: LoginListener, email: String, password: String ){
        Log.d(TAG, "Called for Response!")
        Client().getInstance()?.getGetApi()?.loginWithRESTAPI(LoginRequest(email, password, true))
        ?.enqueue(object: Callback<LoginResponse>{

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    response.body()?.registered
                    Log.d(TAG, "Got Response! Response = ${response.body()?.registered}")
                    listener.getLoginDone(response.body(), true, "User Logged Rest Api")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(TAG, "Failed Response!")
                listener.getLoginDone(null, false, t.message.toString())
            }
        })
    }
}