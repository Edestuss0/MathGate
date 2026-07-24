package com.mathgate.app.features.exam.data.remote.source

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.mathgate.app.core.app.API_URL
import com.mathgate.app.core.exception.AppException
import com.mathgate.app.features.exam.data.remote.dto.ExamQuestionDto
import com.mathgate.app.features.exam.data.remote.dto.ExamThemeDto
import com.mathgate.app.features.exam.data.remote.dto.toDomain
import com.mathgate.app.features.exam.domain.entity.ExamBlock
import com.mathgate.app.features.exam.domain.entity.ExamBlockType
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.shared.exam.entity.ExamTypes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.isSuccess
import io.ktor.util.encodeBase64
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ExamRemoteSource @Inject constructor(
    private val client: HttpClient,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getExamQuestion(type: ExamTypes): ExamQuestion {
        val response = client.get("$API_URL/api/exam?type=${(type.toString()).lowercase()}")
        if (!(response.status.isSuccess())) throw AppException.Network.ServerError(response.status.value)
        val entity = response.body<ExamQuestionDto>().toDomain().copy()
        val blocks = entity.blocks.map {
            if (it.type != ExamBlockType.TEXT) {
                it.copy(content = "$API_URL/api${it.content}")
            } else it
        }
        val solutionBlocks = entity.solutionBlocks.map {
            if (it.type != ExamBlockType.TEXT) {
                it.copy(content = "$API_URL/api${it.content}")
            } else it
        }
        Log.d("BLOCKS", blocks.toString())
        return entity.copy(blocks = blocks, solutionBlocks = solutionBlocks)
    }

    suspend fun getExamQuestionByNumber(type: ExamTypes, number: Int): ExamQuestion {
        val response = client.get("$API_URL/api/exam?type=${(type.toString()).lowercase()}&number=${number}")
        if (!(response.status.isSuccess())) throw AppException.Network.ServerError(response.status.value)
        val entity = response.body<ExamQuestionDto>().toDomain().copy()
        val blocks = entity.blocks.map {
            if (it.type != ExamBlockType.TEXT) {
                it.copy(content = "$API_URL/api${it.content}")
            } else it
        }
        val solutionBlocks = entity.solutionBlocks.map {
            if (it.type != ExamBlockType.TEXT) {
                it.copy(content = "$API_URL/api${it.content}")
            } else it
        }
        Log.d("BLOCKS", blocks.toString())
        return entity.copy(blocks = blocks, solutionBlocks = solutionBlocks)
    }

    suspend fun getExamThemes(type: ExamTypes): List<ExamTheme> {
        val response = client.get("$API_URL/api/exam/themes?type=${(type.toString()).lowercase()}")
        if (!(response.status.isSuccess())) throw AppException.Network.ServerError(response.status.value)
        return response.body<List<ExamThemeDto>>().map { it.toDomain() }
    }

    suspend fun downloadImagesForBlocks(blocks: List<ExamBlock>): List<ExamBlock> {
        val imageBlocks = blocks.filter { it.type != ExamBlockType.TEXT }
        val textBlocks = blocks.filter { it.type == ExamBlockType.TEXT }
        val downloadedImageBlocks = coroutineScope {
            imageBlocks.map { block ->
                async(ioDispatcher) {
                    try {
                        val imageBytes = client.get(block.content).bodyAsBytes()
                        val base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                        val final = "data:image/jpeg;base64,$base64String"
                        block.copy(content = final)
                    } catch (_: Exception) {
                        block
                    }
                }
            }.awaitAll()
        }
        return textBlocks + downloadedImageBlocks
    }
}