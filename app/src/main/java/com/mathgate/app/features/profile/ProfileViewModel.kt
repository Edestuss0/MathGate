package com.mathgate.app.features.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.user.User
import com.mathgate.app.core.data.user.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)

    val state: StateFlow<User> = userRepository.getProfile().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = User.init()
        )

    fun deleteAccount() {
        viewModelScope.launch {
            userRepository.deleteAccount()
        }
    }
}