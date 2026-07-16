@file:OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
package com.mathgate.app.features.exam.repository

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.features.exam.data.local.questions.source.ExamQuestionsLocalSource
import com.mathgate.app.features.exam.data.local.themes.source.ExamThemeLocalSource
import com.mathgate.app.features.exam.data.remote.source.ExamRemoteSource
import com.mathgate.app.features.exam.data.repository.ExamRepositoryImpl
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.shared.exam.entity.ExamTypes
import kotlinx.coroutines.test.StandardTestDispatcher
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

class ExamRepositoryImplTest {
    private lateinit var localQuestions: ExamQuestionsLocalSource
    private lateinit var localThemes: ExamThemeLocalSource
    private lateinit var remote: ExamRemoteSource
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: ExamRepositoryImpl

    @Before
    fun setUp() {
        localQuestions = mockk(relaxed = true)
        localThemes = mockk(relaxed = true)
        remote = mockk()
        repository =
            ExamRepositoryImpl(localThemes = localThemes, localQuestions = localQuestions,  remote = remote, ioDispatcher = testDispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getExamQuestion with internet, full success`() = runTest(testDispatcher) {
        val type = ExamTypes.EGE
        val exceptedQuestion = mockk<ExamQuestion>(relaxed = true)
        coEvery { remote.getExamQuestion(type) } returns exceptedQuestion

        val results = mutableListOf<AppResult<ExamQuestion>>()
        repository.getExamQuestion(type).toList(results)

        advanceUntilIdle()

        assertEquals(2, results.size)
        assertTrue(results[0] is AppResult.Loading)
        assertTrue(results[1] is AppResult.Success)
    }

    @Test
    fun `getExamQuestion without internet semi-success with cache`() = runTest(testDispatcher) {
        val type = ExamTypes.EGE
        val exceptedQuestion = mockk<ExamQuestion>(relaxed = true)

        coEvery { localQuestions.get(type) } returns exceptedQuestion

        val results = mutableListOf<AppResult<ExamQuestion>>()
        repository.getExamQuestion(type).toList(results)

        advanceUntilIdle()

        assertEquals(2, results.size)
        assertTrue(results[0] is AppResult.Loading)
        assertTrue(results[1] is AppResult.Success)
    }

    @Test
    fun `getExamQuestion without result`() = runTest(testDispatcher) {
        val type = ExamTypes.EGE

        coEvery { localQuestions.get(type) } returns null

        val results = mutableListOf<AppResult<ExamQuestion>>()
        repository.getExamQuestion(type).toList(results)

        advanceUntilIdle()

        assertEquals(2, results.size)
        assertTrue(results[0] is AppResult.Loading)
        assertTrue(results[1] is AppResult.Error)
    }
}