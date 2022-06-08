package com.example.fundoonotes.api

object Constant {
    private lateinit var userID: String
    private var constant:Constant? = null

    fun getInstance(): Constant? {
        if(constant == null){
            constant = Constant
        }
        return constant
    }

    fun setUserID(userID: String){
        this.userID = userID
    }
}