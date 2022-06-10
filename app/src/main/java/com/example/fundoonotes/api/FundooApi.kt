package com.example.fundoonotes.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface FundooApi {

    @POST("./accounts:signInWithPassword?key=AIzaSyAtKEmfAzykm_7WcC4WHnUhjRsF-Ref44o")
    fun loginWithRESTAPI(@Body request: LoginRequest): Call<LoginResponse>

}