package com.sanket.donatetoday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.User
import com.sanket.donatetoday.modules.common.data.CreditCardData
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.repository.OnBoardingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(private val onBoardingRepository: OnBoardingRepository) :
    ViewModel() {
    private var _user = MutableStateFlow(User())
    val user = _user.asStateFlow()

    private var _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn = _isUserLoggedIn.asStateFlow()


    fun isUserLoggedIn() = viewModelScope.launch {
        _isUserLoggedIn.update {
            onBoardingRepository.isUserLoggedIn()
        }
    }

    fun updateUserData(
        emailAddress: String? = null,
        password: String? = null,
        telephoneNo: String? = null,
        countryCode: String? = null,
        cardData: CreditCardData? = null,
        donationItemTypes: List<DonationItemTypes>? = null,
        userType: UserType? = null
    ) =
        _user.update {
            var newUser = it
            if (emailAddress != null)
                newUser = it.copy(emailAddress = emailAddress)
            if (password != null)
                newUser = it.copy(password = password)
            if (telephoneNo != null)
                newUser = it.copy(phoneNo = telephoneNo)
            if (countryCode != null)
                newUser = it.copy(countryPhoneCode = countryCode)
            if (cardData != null)
                newUser = it.copy(cardInfo = cardData)
            if (donationItemTypes != null)
                newUser = it.copy(donationItemTypes = donationItemTypes.map { item -> item.type })
            if (userType != null)
                newUser = it.copy(userType = userType.type)
            newUser
        }


    fun updateUserData(user: User) = _user.update { user }

}