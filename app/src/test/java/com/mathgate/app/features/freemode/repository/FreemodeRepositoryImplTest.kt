package com.mathgate.app.features.freemode.repository

import com.mathgate.app.features.freemode.data.repository.FreemodeRepositoryImpl
import com.mathgate.app.features.freemode.data.source.FreemodeLocalSource
import com.mathgate.app.features.freemode.domain.entity.FreemodeBlockType
import com.mathgate.app.shared.freemode.entity.FreemodeDifficulty
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestion
import com.mathgate.app.features.freemode.domain.entity.FreemodeQuestionBlock
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue

class FreemodeRepositoryImplTest {

    private val source: FreemodeLocalSource = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    lateinit private var repository: FreemodeRepositoryImpl

    @Before
    fun setUp() {
        repository = FreemodeRepositoryImpl(source)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getQuestion easy`() = runTest(testDispatcher) {
        val arithmetic = FreemodeQuestion(
            answer =(1 * 10).toString(),
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("1 * 10", FreemodeBlockType.LATEX),
            )
        )
        val fraction = FreemodeQuestion(
            answer = "8",
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("\\frac{4}{1} + \\frac{4]{1}", FreemodeBlockType.LATEX),
            )
        )
        every { source.generateArithmetic() } returns arithmetic
        every { source.generateSimpleFraction() } returns fraction

        val result = repository.getQuestion(FreemodeDifficulty.EASY)

        assertTrue(result.answer == "8" || result.answer == "10")
        assertTrue(result.blocks.isNotEmpty())
    }

    @Test
    fun `getQuestion medium`() = runTest(testDispatcher) {
        val trig = FreemodeQuestion(
            answer = "0.5",
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("sin 30", FreemodeBlockType.LATEX),
            )
        )
        val log = FreemodeQuestion(
            answer = "2",
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("\\log_{5}{25}", FreemodeBlockType.LATEX),
            )
        )
        val quar = FreemodeQuestion(
            answer = "0",
            blocks = listOf(
                FreemodeQuestionBlock("Найдите наименьший корень уравнения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("x^2+2x-3=0", FreemodeBlockType.LATEX),
            )
        )
        every { source.generateSimpleTrigonometry() } returns trig
        every { source.generateSimpleLogarithm() } returns log
        every { source.generateQuadraticEquation() } returns quar

        val result = repository.getQuestion(FreemodeDifficulty.MEDIUM)

        assertTrue(result.answer == "0.5" || result.answer == "0" || result.answer == "2")
        assertTrue(result.blocks.isNotEmpty())
    }

    @Test
    fun `getQuestion hard`() = runTest(testDispatcher) {
        val trig = FreemodeQuestion(
            answer = "6",
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("sin k \\cdot \\frac{\\pi}{6}=0.5", FreemodeBlockType.LATEX),
            )
        )
        val log1 = FreemodeQuestion(
            answer = "50",
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("\\log_{5}{x-25}=2", FreemodeBlockType.LATEX),
            )
        )
        val log2 = FreemodeQuestion(
            answer = "2",
            blocks = listOf(
                FreemodeQuestionBlock("Найдите значение выражения", FreemodeBlockType.TEXT),
                FreemodeQuestionBlock("\\log_{5}{x-25}=\\log_{5}{x-25}", FreemodeBlockType.LATEX),
            )
        )
        every { source.generateSimpleTrigonometryEquation() } returns trig
        every { source.generateEasyLogarithmicEquation() } returns log1
        every { source.generateMediumLogarithmicEquation() } returns log2

        val result = repository.getQuestion(FreemodeDifficulty.HARD)

        assertTrue(result.answer == "2" || result.answer == "50" || result.answer == "6")
        assertTrue(result.blocks.isNotEmpty())
    }

}