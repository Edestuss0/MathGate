package com.mathgate.app.features.profile

import com.mathgate.app.domain.user.entity.User

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false
)
