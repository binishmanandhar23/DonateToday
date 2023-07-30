package com.sanket.donatetoday.viewmodel

import androidx.lifecycle.ViewModel
import com.sanket.donatetoday.navigators.Screen
import com.sanket.donatetoday.navigators.data.ScreenNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel: ViewModel() {
    private var _currentScreen = MutableStateFlow<ScreenNavigator?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    fun goToScreen(screenNavigator: ScreenNavigator) = _currentScreen.update { screenNavigator }
}