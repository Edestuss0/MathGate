package com.mathgate.app.features.start_page

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mathgate.app.core.data.user.UserRepository
import kotlinx.coroutines.launch

class StartPageViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)

    fun register(name: String) {
        viewModelScope.launch {
            userRepository.register(name)
        }
    }

}