package com.example.fundoonotes.model

import java.util.regex.Pattern

class Validator {
    private var validateName = "^[A-Z]\\w{2,12}$"
    private var validateEmail = "^[\\w+_-]+(?:\\.[\\w+_-]+)*[@][\\w]{1,}([.]{1}[a-z]{2,}){1,2}$"
    var validatePhone = "^(91)\\s\\d{10}"
    var validatePassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"


    fun validateName(name: String?): Boolean{
        if(name == null){
            return false
        }
        return Pattern.matches(validateName, name)
    }

    fun validateEmail(email: String?): Boolean{
        if(email == null){
            return false
        }
        return Pattern.matches(validateEmail, email)
    }

    fun validatePassword(password: String?): Boolean{
        if(password == null){
            return false
        }
        return Pattern.matches(validatePassword, password)
    }
}