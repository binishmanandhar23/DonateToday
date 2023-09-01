package com.sanket.donatetoday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanket.donatetoday.enums.UserType
import com.sanket.donatetoday.models.dto.AllDonationTypeDTO
import com.sanket.donatetoday.models.dto.DonationItemUserModel
import com.sanket.donatetoday.models.dto.UserDTO
import com.sanket.donatetoday.models.dto.toUserDTO
import com.sanket.donatetoday.models.dto.toUserEntity
import com.sanket.donatetoday.modules.common.enums.DonationItemTypes
import com.sanket.donatetoday.modules.organization.data.ClothesDonationData
import com.sanket.donatetoday.modules.organization.data.OrganizationDonorCashChartData
import com.sanket.donatetoday.modules.organization.data.OrganizationDonorChartData
import com.sanket.donatetoday.navigators.data.ScreenNavigator
import com.sanket.donatetoday.repository.SharedRepository
import com.sanket.donatetoday.ui.states.HomeUIState
import com.sanket.donatetoday.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel() {

    private var _homeUIState = MutableStateFlow<HomeUIState<UserDTO>?>(null)
    val homeUIState = _homeUIState.asStateFlow()

    private var _currentScreen = MutableStateFlow<ScreenNavigator?>(null)
    val currentScreen = _currentScreen.asStateFlow()

    private var _user = MutableStateFlow(UserDTO())
    val user = _user.asStateFlow()

    private var _listOfAllStatements = MutableStateFlow(AllDonationTypeDTO())
    val listOfAllStatements = _listOfAllStatements.asStateFlow()

    private var _filteredListOfAllStatements = MutableStateFlow(AllDonationTypeDTO())
    val filteredListOfAllStatements = _filteredListOfAllStatements.asStateFlow()

    private var _listOfRecommended = MutableStateFlow(emptyList<DonationItemUserModel>())
    val listOfRecommended = _listOfRecommended.asStateFlow()

    private var _organizationCashChartData =
        MutableStateFlow(emptyList<OrganizationDonorCashChartData>())
    val organizationCashChartData = _organizationCashChartData.asStateFlow()

    private var _donorCashChartData =
        MutableStateFlow(emptyList<OrganizationDonorCashChartData>())
    val donorCashChartData = _donorCashChartData.asStateFlow()

    private var _organizationDonorChartData =
        MutableStateFlow(emptyList<OrganizationDonorChartData>())
    val organizationDonorChartData = _organizationDonorChartData.asStateFlow()

    private var _year = MutableStateFlow(LocalDate.now().year)
    val year = _year.asStateFlow()

    private var _selectedStatementType = MutableStateFlow<DonationItemTypes?>(null)
    val selectedStatementType = _selectedStatementType.asStateFlow()

    init {
        viewModelScope.launch {
            year.collect {
                getDonationReceivedUpdates()
                getDonationDonatedUpdates()
            }
        }
    }

    fun clearData() {
        _currentScreen.update { null }
        _homeUIState.update { null }
    }


    fun changeYear(year: Int) = _year.update { year }

    fun updateSelectedStatementType(statementType: DonationItemTypes?) = _selectedStatementType.update { statementType }

    fun getUser(email: String?) = viewModelScope.launch {
        sharedRepository.getUserFromRealm(email = email).collect { results ->
            when (results) {
                is UpdatedResults, is InitialResults ->
                    results.list.firstOrNull()?.let {
                        _user.update { _ ->
                            it.toUserDTO()
                        }.also {
                            getRecommendedOrganizations()
                            getStatements()
                            getDonationReceivedUpdates()
                            getDonationDonatedUpdates()
                        }
                    }
            }
        }
    }

    private var updateJob: Job? = null
    fun updateUser(userDTO: UserDTO) = viewModelScope.launch {
        //_user.update { userDTO }
        sharedRepository.saveUserToRealm(userEntity = userDTO.toUserEntity())
    }.also {
        if (updateJob?.isActive == true)
            updateJob?.cancel()
        updateJob = viewModelScope.launch(Dispatchers.Default) {
            delay(500)
            sharedRepository.updateUserInFirebase(userDTO = userDTO)
        }
    }

    fun goToScreen(screenNavigator: ScreenNavigator?) = _currentScreen.update { screenNavigator }


    private fun getRecommendedOrganizations() = viewModelScope.launch {
        _listOfRecommended.update {
            sharedRepository.getRecommendedListOfOrganizations(userDTO = user.value)
        }
    }

    fun getOrganizationBasedOnId(id: String) = viewModelScope.launch {
        _homeUIState.update { HomeUIState.Loading() }
        try {
            val organizationDTO = sharedRepository.getOrganizationBasedOnId(id = id)
            _homeUIState.update { HomeUIState.Success(data = organizationDTO) }
        } catch (ex: Exception) {
            _homeUIState.update { HomeUIState.Error(errorMessage = ex.message) }
        }
    }

    fun getUserBasedOnId(id: String) = viewModelScope.launch {
        _homeUIState.update { HomeUIState.Loading() }
        try {
            val userDTO = sharedRepository.getUserBasedOnId(id = id)
            _homeUIState.update { HomeUIState.Success(data = userDTO) }
        } catch (ex: Exception) {
            _homeUIState.update { HomeUIState.Error(errorMessage = ex.message) }
        }
    }

    fun addCashDonation(amount: Int) = viewModelScope.launch {
        when (val state = homeUIState.value) {
            is HomeUIState.Success -> {
                state.data?.let { organizationDTO ->
                    try {
                        sharedRepository.addCashDonation(
                            userDTO = user.value,
                            organization = organizationDTO,
                            amount = amount
                        )
                        val userDTO =
                            sharedRepository.getUserFromFirebase(email = user.value.emailAddress)
                        updateUser(userDTO)
                        getOrganizationBasedOnId(organizationDTO.id)
                    } catch (ex: Exception) {
                        ex.message
                    }
                }
            }

            else -> Unit
        }
    }

    fun addClothesDonation(clothesDonationData: List<ClothesDonationData>) = viewModelScope.launch {
        when (val state = homeUIState.value) {
            is HomeUIState.Success -> {
                state.data?.let { organizationDTO ->
                    try {
                        sharedRepository.addClothesDonation(
                            userDTO = user.value,
                            organization = organizationDTO,
                            clothesDonationData = clothesDonationData
                        )
                        val userDTO =
                            sharedRepository.getUserFromFirebase(email = user.value.emailAddress)
                        updateUser(userDTO)
                        getOrganizationBasedOnId(organizationDTO.id)
                    } catch (ex: Exception) {
                        ex.message
                    }
                }
            }

            else -> Unit
        }
    }

    private fun getStatements() = viewModelScope.launch(Dispatchers.IO) {
        try {
            sharedRepository.getStatementsAsynchronously(user = user.value, onSuccess = {
                _listOfAllStatements.update { _ -> it }
            }, onError = {
                throw Exception(it)
            })
        } catch (ex: Exception) {
            _homeUIState.update { HomeUIState.Error(errorMessage = ex.message) }
        }
    }

    fun filterStatements(search: String) = viewModelScope.launch(Dispatchers.IO) {
        _filteredListOfAllStatements.update { _ ->
            listOfAllStatements.value.let { dto ->
                dto.copy(cash = dto.cash.filter {
                    if (search.isEmpty())
                        true
                    else if (user.value.userType == UserType.Donor.type)
                        it.organizationName.contains(search, ignoreCase = true)
                    else
                        it.userName.contains(search, ignoreCase = true)
                }, food = dto.food.filter {
                    if (search.isEmpty())
                        true
                    else if (user.value.userType == UserType.Donor.type)
                        it.organizationName.contains(search, ignoreCase = true)
                    else
                        it.userName.contains(search, ignoreCase = true)
                }, clothes = dto.clothes.filter {
                    if (search.isEmpty())
                        true
                    else if (user.value.userType == UserType.Donor.type)
                        it.organizationName.contains(search, ignoreCase = true)
                    else
                        it.userName.contains(search, ignoreCase = true)
                }, utensils = dto.utensils.filter {
                    if (search.isEmpty())
                        true
                    else if (user.value.userType == UserType.Donor.type)
                        it.organizationName.contains(search, ignoreCase = true)
                    else
                        it.userName.contains(search, ignoreCase = true)
                }, all = dto.all.filter {
                    if (search.isEmpty())
                        true
                    else if (user.value.userType == UserType.Donor.type)
                        it.organizationName.contains(search, ignoreCase = true)
                    else
                        it.userName.contains(search, ignoreCase = true)
                })
            }
        }
    }

    private fun getDonationReceivedUpdates() = viewModelScope.launch(Dispatchers.IO) {
        sharedRepository.getStatementsAsynchronously(
            user = user.value,
            onSuccess = { statements ->
                val arrayListOfCashChartData = ArrayList<OrganizationDonorCashChartData>()
                for (i in 1..12) {
                    val totalAmount = statements.cash.filter { statement ->
                        DateUtils.convertMainDateTimeFormatToLocalDate(
                            date = statement.date
                        )!!.let {
                            it.year == this@SharedViewModel.year.value && it.monthValue == i && statement.donationType == DonationItemTypes.Cash.type
                        }
                    }.sumOf { it.amount?: 0 }
                    arrayListOfCashChartData.add(
                        OrganizationDonorCashChartData(
                            localDate = LocalDate.of(
                                year.value,
                                i,
                                1
                            ), amount = totalAmount
                        )
                    )
                }
                _organizationCashChartData.update { arrayListOfCashChartData }
                val arrayListOfDonorChartData = ArrayList<OrganizationDonorChartData>()
                statements.cash.groupBy { it.userId }.forEach { (_, statements) ->
                    if (statements.all { statement ->
                            DateUtils.convertMainDateTimeFormatToLocalDate(
                                date = statement.date
                            )!!.let {
                                it.year == this@SharedViewModel.year.value
                            }
                        })
                        arrayListOfDonorChartData.add(
                            OrganizationDonorChartData(
                                donor = statements.first().userName,
                                amount = statements.sumOf { it.amount?: 0 })
                        )
                }
                _organizationDonorChartData.update {
                    if (arrayListOfDonorChartData.size > 4)
                        arrayListOfDonorChartData.subList(0, 4).sortedByDescending { it.amount }
                    else
                        arrayListOfDonorChartData.sortedByDescending { it.amount }
                }
            },
            onError = {
                _homeUIState.update { _ -> HomeUIState.Error(errorMessage = it) }
            })
    }

    private fun getDonationDonatedUpdates() = viewModelScope.launch(Dispatchers.IO) {
        sharedRepository.getStatementsAsynchronously(
            user = user.value,
            onSuccess = { statements ->
                val arrayListOfCashChartData = ArrayList<OrganizationDonorCashChartData>()
                for (i in 1..12) {
                    val totalAmount = statements.cash.filter { statement ->
                        DateUtils.convertMainDateTimeFormatToLocalDate(
                            date = statement.date
                        )!!.let {
                            it.year == this@SharedViewModel.year.value && it.monthValue == i && statement.donationType == DonationItemTypes.Cash.type
                        }
                    }.sumOf { it.amount?: 0 }
                    arrayListOfCashChartData.add(
                        OrganizationDonorCashChartData(
                            localDate = LocalDate.of(
                                year.value,
                                i,
                                1
                            ), amount = totalAmount
                        )
                    )
                }
                _donorCashChartData.update { arrayListOfCashChartData }
            },
            onError = {
                _homeUIState.update { _ -> HomeUIState.Error(errorMessage = it) }
            })
    }
}