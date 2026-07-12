package com.mathgate.app.domain.exam.repository

import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.core.exception.ServerException
import com.mathgate.app.domain.exam.data.local.questions.source.ExamQuestionsLocalSource
import com.mathgate.app.domain.exam.data.local.themes.source.ExamThemeLocalSource
import com.mathgate.app.domain.exam.data.remote.source.ExamRemoteSource
import com.mathgate.app.domain.exam.entity.ExamQuestion
import com.mathgate.app.domain.exam.entity.ExamTheme
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
    private val localQuestions: ExamQuestionsLocalSource,
    private val localThemes: ExamThemeLocalSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ExamRepository {

    val scope = CoroutineScope(SupervisorJob() + ioDispatcher)

    override suspend fun getExamQuestion(type: ExamTypes): Flow<AppResult<ExamQuestion>> = flow {
        emit(AppResult.Loading)
        try {
            val response = remote.getExamQuestion(type)
            localQuestions.insert(response, type)
            emit(AppResult.Success(response))
            repeat(5) {
                scope.launch {
                    val toCache = remote.getExamQuestion(type)
                    localQuestions.insert(toCache, type)
                }
            }
        } catch (e: Exception) {
            val cached = localQuestions.get(type)
            if (cached != null) {
                localQuestions.invalidate(cached.id)
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



    override suspend fun getExamThemes(type: ExamTypes): Flow<AppResult<List<ExamTheme>>> = flow {
        val cached = localThemes.get(type)
        if (cached.isNotEmpty()) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remote.getExamThemes(type)
            emit(AppResult.Success(response))
            localThemes.insertMore(response, type)
        } catch (e: Exception) {
            when {
                e is CancellationException -> throw e
                e is ServerException -> {
                    emit(
                        AppResult.Error(
                            message = "Ошибка при попытке получения тем",
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

    override suspend fun getExamQuestionByNumber(type: ExamTypes, number: Int): Flow<AppResult<ExamQuestion>> = flow {
        emit(AppResult.Loading)
        try {
            val response = remote.getExamQuestionByNumber(type, number)
            localQuestions.insert(response, type)
            emit(AppResult.Success(response))
            repeat(5) {
                scope.launch {
                    val toCache = remote.getExamQuestionByNumber(type, number)
                    localQuestions.insert(toCache, type)
                }
            }
        } catch (e: Exception) {
            val cached = localQuestions.getByNumber(type, number)
            if (cached != null) {
                localQuestions.invalidate(cached.id)
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