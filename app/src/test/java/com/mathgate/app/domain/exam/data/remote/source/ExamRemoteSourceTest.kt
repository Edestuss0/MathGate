package com.mathgate.app.domain.exam.data.remote.source

import com.mathgate.app.core.api.ApiClient
import com.mathgate.app.core.exception.ServerException
import com.mathgate.app.domain.exam.data.remote.dto.ExamBlockDto
import com.mathgate.app.domain.exam.data.remote.dto.ExamQuestionDto
import com.mathgate.app.domain.exam.entity.ExamBlock
import com.mathgate.app.domain.exam.entity.ExamTypes
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test

class ExamRemoteSourceTest {

    private lateinit var client: ApiClient
    private lateinit var source: ExamRemoteSource
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        client = mockk<ApiClient>(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getFromApi full success`() = runTest(testDispatcher) {
        val type = ExamTypes.EGE
        val bodyMock = ExamQuestionDto(
            id = 24,
            answer = "sdfs",
            blocks = emptyList(),
            solutionBlocks = listOf(
                ExamBlockDto(type = "TEXT", content = "sfdsdf")
            ),
        )
        val jsonResponse = Json.encodeToString(bodyMock)
        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        every { client.client } returns httpClient

        source = ExamRemoteSource(client)

        val result = source.getExamQuestion(type)

        assertEquals(result.id, 24)
        assertEquals(result.blocks, emptyList<ExamBlock>())
        assertEquals(result.answer, "sdfs")
    }

    @Test
    fun `getFromApi error`() = runTest(testDispatcher) {
        val type = ExamTypes.EGE
        val bodyMock = ExamQuestionDto(
            id = 24,
            answer = "sdfs",
            blocks = emptyList(),
            solutionBlocks = listOf(
                ExamBlockDto(type = "TEXT", content = "sfdsdf")
            ),
        )
        val jsonResponse = Json.encodeToString(bodyMock)
        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonResponse,
                status = HttpStatusCode.NotFound,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        every { client.client } returns httpClient

        source = ExamRemoteSource(client)

        val exception = org.junit.Assert.assertThrows(ServerException::class.java) {
            runBlocking { source.getExamQuestion(type) }
        }

        assertEquals(exception.code, 404)
    }
}