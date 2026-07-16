package com.mathgate.app.features.exam.data.remote.source

import com.mathgate.app.core.app.API_URL
import com.mathgate.app.core.exception.AppException
import com.mathgate.app.features.exam.data.remote.dto.ExamQuestionDto
import com.mathgate.app.features.exam.data.remote.dto.ExamThemeDto
import com.mathgate.app.features.exam.data.remote.dto.toDomain
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.shared.exam.entity.ExamTypes
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

class ExamRemoteSource @Inject constructor(
    private val client: HttpClient
) {

    suspend fun getExamQuestion(type: ExamTypes): ExamQuestion {
        val response = client.get("$API_URL/api/exam?type=${(type.toString()).lowercase()}")
        if (!(response.status.isSuccess())) throw AppException.Network.ServerError(response.status.value)
        return response.body<ExamQuestionDto>().toDomain()
    }

    suspend fun getExamQuestionByNumber(type: ExamTypes, number: Int): ExamQuestion {
        val response = client.get("$API_URL/api/exam?type=${(type.toString()).lowercase()}&number=${number}")
        if (!(response.status.isSuccess())) throw AppException.Network.ServerError(response.status.value)
        return response.body<ExamQuestionDto>().toDomain()
    }

    suspend fun getExamThemes(type: ExamTypes): List<ExamTheme> {
        val response = client.get("$API_URL/api/exam/themes?type=${(type.toString()).lowercase()}")
        if (!(response.status.isSuccess())) throw AppException.Network.ServerError(response.status.value)
        return response.body<List<ExamThemeDto>>().map { it.toDomain() }
    }
}