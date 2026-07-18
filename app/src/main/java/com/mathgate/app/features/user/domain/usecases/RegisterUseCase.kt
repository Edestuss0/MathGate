package com.mathgate.app.features.user.domain.usecases

import com.mathgate.app.core.analytics.core.AnalyticsManager
import com.mathgate.app.shared.user.domain.repository.IUserRepository
import javax.inject.Inject
import kotlin.uuid.Uuid

class RegisterUseCase @Inject constructor(
    private val repository: IUserRepository,
    private val analytics: AnalyticsManager
) {
    suspend operator fun invoke(name: String) {
        repository.register(name)
        analytics.setUserId(Uuid.random().toString())
        analytics.setUserUsername(name)
    }
}