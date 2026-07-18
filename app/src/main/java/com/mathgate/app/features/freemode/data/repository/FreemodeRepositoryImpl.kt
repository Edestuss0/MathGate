package com.mathgate.app.features.freemode.data.repository

import com.mathgate.app.features.freemode.data.source.FreemodeLocalSource
import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import com.mathgate.app.features.freemode.domain.repository.IFreemodeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import kotlin.random.nextInt

@Singleton
class FreemodeRepositoryImpl @Inject constructor(
    private val source: FreemodeLocalSource
) : IFreemodeRepository {

    val freemodeGenerators = mapOf<FreemodeDifficulty, List<() -> FreemodeQuestion>>(
        FreemodeDifficulty.EASY to listOf(source::generateArithmetic, source::generateSimpleFraction),
        FreemodeDifficulty.MEDIUM to listOf(source::generateSimpleLogarithm, source::generateSimpleTrigonometry, source::generateQuadraticEquation),
        FreemodeDifficulty.HARD to listOf(source::generateEasyLogarithmicEquation, source::generateMediumLogarithmicEquation, source::generateSimpleTrigonometryEquation)
    )

    override fun getQuestion(difficulty: FreemodeDifficulty): FreemodeQuestion {
        val generator = freemodeGenerators[difficulty]?.random()
            ?: throw IllegalArgumentException("Нет генераторов для сложности: ${difficulty.label}")
        return generator()
    }


}