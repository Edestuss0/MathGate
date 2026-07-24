package com.mathgate.app.shared.user.domain.usecases

import com.mathgate.app.core.di.ApplicationScope
import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.shared.user.domain.repository.IUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: IUserRepository,
) {
    operator fun invoke(): StateFlow<User?> = repository.getUser()
}