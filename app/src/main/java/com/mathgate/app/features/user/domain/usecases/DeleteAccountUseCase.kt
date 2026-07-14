package com.mathgate.app.features.user.domain.usecases

import com.mathgate.app.shared.user.domain.repository.IUserRepository
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    suspend operator fun invoke() {
        repository.deleteAccount()
    }
}