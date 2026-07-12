package com.mathgate.app.domain.exam.data.remote.source

import com.mathgate.app.core.api.ApiClient
import com.mathgate.app.core.app.API_URL
import com.mathgate.app.core.exception.ServerException
import com.mathgate.app.domain.exam.data.remote.dto.ExamQuestionDto
import com.mathgate.app.domain.exam.data.remote.dto.ExamThemeDto
import com.mathgate.app.domain.exam.data.remote.dto.toDomain
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTheme
import com.mathgate.app.domain.exam.entity.ExamTypes
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

class ExamRemoteSource @Inject constructor(
    private val apiClient: ApiClient
) {
    val client = apiClient.client

    suspend fun getExamQuestion(type: ExamTypes): ExamQuestion {
        val response = client.get("$API_URL/api/exam?type=${(type.toString()).lowercase()}")
        if (!(response.status.isSuccess())) throw ServerException(response.status.value)
        return response.body<ExamQuestionDto>().toDomain()
    }

    suspend fun getExamQuestionByNumber(type: ExamTypes, number: Int): ExamQuestion {
        val response = client.get("$API_URL/api/exam?type=${(type.toString()).lowercase()}?number=${number}")
        if (!(response.status.isSuccess())) throw ServerException(response.status.value)
        return response.body<ExamQuestionDto>().toDomain()
    }

    suspend fun getExamThemes(type: ExamTypes): List<ExamTheme> {
        val response = client.get("$API_URL/api/exam/themes?type=${(type.toString()).lowercase()}")
        if (!(response.status.isSuccess())) throw ServerException(response.status.value)
        return response.body<List<ExamThemeDto>>().map { it.toDomain() }
    }
}