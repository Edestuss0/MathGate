package com.mathgate.app.features.freemode.data.repository

import com.mathgate.app.features.freemode.data.source.FreemodeLocalSource
import com.mathgate.app.features.freemode.domain.entity.FreemodeDifficulty
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
    override fun getQuestion(difficulty: FreemodeDifficulty): FreemodeQuestion {
        return when (difficulty) {
            FreemodeDifficulty.EASY -> {
                val random = Random.nextInt(1..2)
                when (random) {
                    1-> {source.generateArithmetic()}
                    else -> {source.generateSimpleFraction()}
                }
            }

            FreemodeDifficulty.MEDIUM -> {
                val random = Random.nextInt(1..3)
                when (random) {
                    1-> {source.generateSimpleLogarithm()}
                    2 -> {source.generateSimpleTrigonometry()}
                    else -> {source.generateQuadraticEquation()}
                }
            }

            FreemodeDifficulty.HARD -> {
                val random = Random.nextInt(1..3)
                when (random) {
                    1 -> {source.generateEasyLogarithmicEquation()}
                    2 -> {source.generateMediumLogarithmicEquation()}
                    else-> {source.generateSimpleTrigonometryEquation()}
                }
            }
        }
    }


}