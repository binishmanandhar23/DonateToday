package com.sanket.donatetoday.viewmodel

import androidx.lifecycle.ViewModel
import com.sanket.donatetoday.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel: ViewModel() {
    private var _currentScreen = MutableStateFlow<Screen?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    fun goToScreen(screen: Screen) = _currentScreen.update { screen }
}