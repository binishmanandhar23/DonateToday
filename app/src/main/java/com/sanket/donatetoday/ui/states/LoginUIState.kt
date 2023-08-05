package com.sanket.donatetoday.ui.states

sealed class LoginUIState(val message: String? = null){
    object Loading: LoginUIState()
    class Success(successMessage: String? = null): LoginUIState(message = successMessage)
    class Error(errorMessage: String?): LoginUIState(message = errorMessage)
}
