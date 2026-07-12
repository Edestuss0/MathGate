package com.mathgate.app.domain.exam.repository

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.core.exception.ServerException
import com.mathgate.app.domain.exam.data.local.source.ExamLocalSource
import com.mathgate.app.domain.exam.data.remote.source.ExamRemoteSource
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTypes
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepositoryImpl @Inject constructor(
    private val remote: ExamRemoteSource,
    private val local: ExamLocalSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExamRepository {

    val scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    override suspend fun getExamQuestion(type: ExamTypes): Flow<AppResult<ExamQuestion>> = flow {
        emit(AppResult.Loading)
        try {
            val response = remote.getExamQuestion(type)
            local.insert(response, type)
            emit(AppResult.Success(response))
            repeat(5) {
                scope.launch {
                    val toCache = remote.getExamQuestion(type)
                    local.insert(toCache, type)
                }
            }
        } catch (e: Exception) {
            val cached = local.get(type)
            if (cached != null) {
                local.invalidate(cached.id)
                emit(AppResult.Success(cached))
            } else {
                when {
                    e is CancellationException -> throw e
                    e is ServerException -> {
                        emit(
                            AppResult.Error(
                                message = "Ошибка при попытке получения вопроса",
                                data = null
                            )
                        )
                    }

                    e is IOException -> {
                        emit(
                            AppResult.Error(
                                message = "Нет соединения с интернетом",
                                data = null
                            )
                        )
                    }

                    else -> {
                        emit(
                            AppResult.Error(
                                message = "Произошла непредвиденная ошибка",
                                data = null
                            )
                        )
                    }
                }
            }

        }
    }

}