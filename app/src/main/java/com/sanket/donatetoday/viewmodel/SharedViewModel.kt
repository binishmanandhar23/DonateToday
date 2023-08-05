package com.sanket.donatetoday.viewmodel

import androidx.lifecycle.ViewModel
import com.sanket.donatetoday.navigators.Screen
import com.sanket.donatetoday.navigators.data.ScreenNavigator
import com.sanket.donatetoday.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val sharedRepository: SharedRepository): ViewModel() {
    private var _currentScreen = MutableStateFlow<ScreenNavigator?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    fun goToScreen(screenNavigator: ScreenNavigator?) = _currentScreen.update { screenNavigator }
}