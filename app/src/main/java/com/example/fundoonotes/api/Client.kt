package com.example.fundoonotes.api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TAG = "Client"
class Client() {
    private lateinit var fundooAPI: FundooApi
    private var fundooClientInstance: Client? = null
    private lateinit var okHTTPClient: OkHttpClient

    init {
        val httpClient = OkHttpClient.Builder()
        okHTTPClient = httpClient.connectTimeout(50, TimeUnit.SECONDS).readTimeout(50, TimeUnit.SECONDS).
                writeTimeout(50, TimeUnit.SECONDS).build()
    }

    fun getInstance(): Client?{
        if(fundooClientInstance == null){
            fundooClientInstance = Client()
        }
        return fundooClientInstance
    }

    fun getGetApi(): FundooApi{
        Log.d(TAG, "Client Lmao!")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://identitytoolkit.googleapis.com/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHTTPClient)
            .build()

        fundooAPI = retrofit.create(FundooApi::class.java)
        Log.d(TAG, "Got Fundoo Api Lmao!")
        return fundooAPI
    }
}