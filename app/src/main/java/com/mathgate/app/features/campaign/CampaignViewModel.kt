package com.mathgate.app.features.campaign

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.campaign.CampaignEntity
import com.mathgate.app.core.data.campaign.CampaignRepository
import com.mathgate.app.core.data.user.User
import com.mathgate.app.core.data.user.UserRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class CampaignViewModel(private val campaignRepository: CampaignRepository, private val userRepository: UserRepository) : ViewModel() {

    companion object {
        fun provideFactory(campaignRepository: CampaignRepository, userRepository: UserRepository): ViewModelProvider.Factory = object :
            ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return CampaignViewModel(campaignRepository, userRepository) as T
            }
        }
    }

    private val _state = MutableStateFlow(CampaignState())
    val state = _state.asStateFlow()

    private val user = userRepository.getProfile().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = User.init()
    )

    private val allCampaigns = campaignRepository.allCampaigns.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val currentCampaign: StateFlow<CampaignEntity?> = user.flatMapLatest { currentUser ->
        flow {
            val campaign = campaignRepository.getCampaignById(currentUser.current_campaign)
            emit(campaign)
            _state.update { it.copy(currentCampaign = campaign) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val isLastCampaign: StateFlow<Boolean> = currentCampaign.flatMapLatest { campaign ->
        flow {
            val isLast = (campaign?.id ?: 1) >= campaignRepository.getCount()
            emit(isLast)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun onAnswer(answer: Int) {
        viewModelScope.launch {
            val count: Int = campaignRepository.getCount()

            if (answer == currentCampaign.value?.answer ?: 0) {
                if (currentCampaign.value?.id ?: 1 >= count) {
                    _state.update { it.copy(message = "Вы прошли всю кампанию!", isError = false) }
                    return@launch
                }

                viewModelScope.launch {
                    userRepository.completeCampaign()
                }
                _state.update { it.copy(message = "Правильно", isError = false) }
            } else {
                _state.update { it.copy(message = "Неправильно", isError = true) }
            }
        }

    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }

    fun initializeData(context: Context) {
        viewModelScope.launch {
            campaignRepository.checkAndPreloadDb(context.applicationContext)
        }
    }
}