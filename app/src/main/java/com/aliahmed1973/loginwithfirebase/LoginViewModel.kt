package com.aliahmed1973.loginwithfirebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class LoginViewModel: ViewModel() {

    val authenticationState = FirebaseUserLivedata().map { firebaseUser->
        if(firebaseUser!=null)
        {
            AuthenticationState.AUTHENTICATED
        }else
        {
            AuthenticationState.UNAUTHENTICATED
        }
    }
}
enum class AuthenticationState{
AUTHENTICATED,UNAUTHENTICATED
}