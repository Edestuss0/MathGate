package com.mathgate.app.domain.exam.data.remote.source

import com.mathgate.app.core.api.ApiClient
import com.mathgate.app.core.app.API_URL
import com.mathgate.app.core.exception.ServerException
import com.mathgate.app.domain.exam.data.remote.dto.ExamQuestionDto
import com.mathgate.app.domain.exam.data.remote.dto.toDomain
import com.mathgate.app.domain.exam.entity.ExamQuestion
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
}