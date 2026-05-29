package com.mathgate.app

import kotlin.random.Random
import kotlin.random.nextInt

class GenerateMath(val difficulty: String) {
    var question = ""

    var correctAnswer = 0

    init {
        getQuestion()
    }

    private fun difficultyRange(): IntRange {
        return when (difficulty.lowercase()) {
            "easy" -> 1..10
            "medium" -> 7..25
            "hard" -> 15..65
            else -> 7..25
        }
    }

    fun getQuestion() {



        val valueA = Random.nextInt(difficultyRange().first, difficultyRange().last + 1)
        val valueB = Random.nextInt(difficultyRange().first, difficultyRange().last + 1)

        val action = generateAction()

        this.question = "$valueA $action $valueB"

        correctAnswer = when (action) {
            "+" -> valueA + valueB
            "-" -> valueA - valueB
            else -> valueA * valueB
        }

        println("QUESTION: ${this.question}")
        println("ANSWER: ${this.correctAnswer}")
    }

    fun answer(answerValue: String): Boolean {
        if (correctAnswer.toString() == answerValue) {
            getQuestion()
            return true
        }
        return false
    }

    private fun generateAction(): String {
        val random = Random.nextInt(1..3)

        when (random) {
            1 -> return "+"
            2 -> return "-"
            3 -> return "*"
        }

        return "+"
    }

}