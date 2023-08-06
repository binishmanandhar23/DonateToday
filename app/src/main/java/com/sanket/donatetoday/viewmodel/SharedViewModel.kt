package com.sanket.donatetoday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.dto.toUserDTO
import com.sanket.donatetoday.models.dto.toUserEntity
import com.sanket.donatetoday.navigators.Screen
import com.sanket.donatetoday.navigators.data.ScreenNavigator
import com.sanket.donatetoday.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val sharedRepository: SharedRepository): ViewModel() {
    private var _currentScreen = MutableStateFlow<ScreenNavigator?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    private var _user = MutableStateFlow(UserDTO())
    val user = _user.asStateFlow()


    fun getUser(email: String?) = viewModelScope.launch {
        sharedRepository.getUserFromRealm(email = email).collect{ results ->
            when(results){
                is UpdatedResults, is InitialResults ->
                    _user.update {
                        results.list.first().toUserDTO()
                    }
            }
        }
    }

    fun updateUser(userDTO: UserDTO) = viewModelScope.launch {
        //_user.update { userDTO }
        sharedRepository.saveUserToRealm(userEntity = userDTO.toUserEntity())
    }

    fun goToScreen(screenNavigator: ScreenNavigator?) = _currentScreen.update { screenNavigator }
}