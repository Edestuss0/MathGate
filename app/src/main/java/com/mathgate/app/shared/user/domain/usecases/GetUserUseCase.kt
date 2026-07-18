package com.mathgate.app.shared.user.domain.usecases

import com.mathgate.app.shared.user.domain.entity.User
import com.mathgate.app.shared.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: IUserRepository,
) {
    operator fun invoke(): Flow<User> {
        return repository.getUser()
    }
}