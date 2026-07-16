package com.mathgate.app.features.freemode.domain.repository

import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion

interface IFreemodeRepository {
    fun getQuestion(difficulty: FreemodeDifficulty): FreemodeQuestion
}