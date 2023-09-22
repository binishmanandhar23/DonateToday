package com.sanket.donatetoday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.dto.CreditCardDataDTO
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.repository.OnBoardingRepository
import com.sanket.donatetoday.ui.states.LoginUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val onBoardingRepository: OnBoardingRepository) :
    ViewModel() {
    private var _userDTO = MutableStateFlow(UserDTO())
    val user = _userDTO.asStateFlow()

    private var _loginUIState = MutableStateFlow<LoginUIState?>(null)
    val loginUIState = _loginUIState.asStateFlow()


    fun isUserLoggedIn() = viewModelScope.launch {
        if (onBoardingRepository.isUserLoggedIn()) {
            _loginUIState.update { LoginUIState.Loading }
            onBoardingRepository.syncCurrentUserDataToDatabase(onSuccess = {
                _userDTO.update { _ -> it }
                _loginUIState.update { _ ->
                    LoginUIState.Success("Welcome back! ${it.name}")
                }
            }, onError = {
                    _loginUIState.update {
                        LoginUIState.Error(it?.message)
                    }
            })
        }
    }

    fun sendEmailVerification() = onBoardingRepository.sendEmailVerification()

    fun updateUserData(
        emailAddress: String? = null,
        password: String? = null,
        telephoneNo: String? = null,
        countryCode: String? = null,
        cardData: CreditCardDataDTO? = null,
        donationItemTypes: List<DonationItemTypes>? = null,
        userType: UserType? = null,
        emailVerified: Boolean? = false,
        userVerified: Boolean? = false,
    ) =
        _userDTO.update {
            var newUser = it
            if (emailAddress != null)
                newUser = newUser.copy(emailAddress = emailAddress)
            if (password != null)
                newUser = newUser.copy(password = password)
            if (telephoneNo != null)
                newUser = newUser.copy(phoneNo = telephoneNo)
            if (countryCode != null)
                newUser = newUser.copy(countryPhoneCode = countryCode)
            if (cardData != null)
                newUser = newUser.copy(cardInfo = cardData)
            if (donationItemTypes != null)
                newUser =
                    newUser.copy(donationItemTypes = donationItemTypes.map { item -> item.type })
            if (userType != null)
                newUser = newUser.copy(userType = userType.type)
            if (emailVerified != null)
                newUser = newUser.copy(emailVerified = emailVerified)
            if (userVerified != null)
                newUser = newUser.copy(emailVerified = userVerified)
            newUser
        }


    fun updateUserData(userDTO: UserDTO) = _userDTO.update { userDTO }


    fun onSignIn() = viewModelScope.launch {
        _loginUIState.update { LoginUIState.Loading }
        onBoardingRepository.onSignIn(
            email = user.value.emailAddress,
            password = user.value.password,
            onSuccess = {
                _userDTO.update { _ -> it }
                _loginUIState.update { _ ->
                    LoginUIState.Success("Welcome back! ${it.name}")
                }
            },
            onError = {
                _loginUIState.update {
                    LoginUIState.Error(it?.message)
                }
            }
        )
    }

    fun onSignUp() = viewModelScope.launch {
        onBoardingRepository.onSignUp(
            userDTO = user.value
        ) {
            _loginUIState.update { _ -> it }
        }
    }

    fun clearData() = _userDTO.update { UserDTO() }
}