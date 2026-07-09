package com.mathgate.app.domain.freemode.repository

import com.mathgate.app.domain.freemode.entity.FreemodeDifficulty
import com.mathgate.app.domain.freemode.entity.FreemodeQuestion

interface IFreemodeRepository {
    fun getQuestion(difficulty: FreemodeDifficulty): FreemodeQuestion
}