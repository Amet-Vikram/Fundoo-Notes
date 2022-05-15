package com.example.fundoonotes.view

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RegisterTest{

    private val register = Register()

    @Test
    fun userInputBeingValidShouldReturnTrue(){
        val fName = "Amet"
        val lName = "Vikram"
        val email = "abc@gmail.com"
        val password = "Asdf@1234"
        val result = register.validateUserInput(fName, lName, email, password)

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun userNameBeingInValidShouldReturnFalse(){
        val fName = "ae1234!"
        val lName = "Vikram"
        val email = "abc@gmail.com"
        val password = "Asdf@1234"
        val result = register.validateUserInput(fName, lName, email, password)

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun userEMailBeingInValidShouldReturnFalse(){
        val fName = "ae1234!"
        val lName = "Vikram"
        val email = "abc!@!@gmail.com"
        val password = "Asdf@1234"
        val result = register.validateUserInput(fName, lName, email, password)

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun userPasswordBeingInValidShouldReturnFalse(){
        val fName = "ae1234!"
        val lName = "Vikram"
        val email = "abc!@!@gmail.com"
        val password = "asd1234"
        val result = register.validateUserInput(fName, lName, email, password)

        assertThat(result).isEqualTo(false)
    }

}