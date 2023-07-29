package com.sanket.donatetoday.modules.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import com.sanket.donatetoday.modules.onboarding.data.LoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//@HiltViewModel
class OnBoardingViewModel: ViewModel() {
    private var _loginData = MutableStateFlow(LoginData())
    val loginData = _loginData.asStateFlow()

    fun updateLoginData(emailAddress: String? = null , password: String? = null) =
        _loginData.update {
            if(emailAddress != null)
                it.copy(emailAddress = emailAddress)
            else if(password != null)
                it.copy(password = password)
            else
                it
        }
}