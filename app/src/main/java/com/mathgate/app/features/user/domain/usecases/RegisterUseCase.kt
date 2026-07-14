package com.mathgate.app.features.user.domain.usecases

import com.mathgate.app.shared.user.domain.repository.IUserRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(name: String) {
        repository.register(name)
    }
}