package com.sanket.donatetoday.ui.states

sealed class HomeUIState <T> (val data: T? = null, val message: String? = null){
    class Loading <T>: HomeUIState<T>()

    class Success <T>(data: T? = null): HomeUIState<T>(data = data)

    class Error<T>(errorMessage: String? = null): HomeUIState<T>(message = errorMessage)
}
