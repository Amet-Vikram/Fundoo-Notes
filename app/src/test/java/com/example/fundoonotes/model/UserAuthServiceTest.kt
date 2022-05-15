package com.example.fundoonotes.model

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.net.PasswordAuthentication

@RunWith(JUnit4::class)
class UserAuthServiceTest{

    private val userAuthService = UserAuthService()

    @Test
    fun userInputBeingValidShouldReturnTrue(){
        val fName = "Amet"
        val lName = "Vikram"
        val email = "abc@gmail.com"
        val password = "asdf1234"
        userAuthService.userRegister(fName, lName, email, password){
            assertThat(it.status).isEqualTo(true)
        }
    }

    @Test
    fun userNameBeingInValidShouldReturnFalse(){
        val fName = "ae1234!"
        val lName = "Vikram"
        val email = "abc@gmail.com"
        val password = "asdf1234"
        userAuthService.userRegister(fName, lName, email, password){
            assertThat(it.status).isEqualTo(false)
        }
    }

    @Test
    fun userEMailBeingInValidShouldReturnFalse(){
        val fName = "ae1234!"
        val lName = "Vikram"
        val email = "abc!@!@gmail.com"
        val password = "asdf1234"
        userAuthService.userRegister(fName, lName, email, password){
            assertThat(it.status).isEqualTo(false)
        }
    }
}