package com.mathgate.app.shared.user.domain.usecases

import com.mathgate.app.shared.user.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAuthorizedStatusUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.getUser().map { it.registered }
    }
}