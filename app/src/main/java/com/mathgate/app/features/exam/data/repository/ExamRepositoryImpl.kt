package com.mathgate.app.features.exam.data.repository

import android.util.Log
import com.mathgate.app.core.entity.AppResult
import com.mathgate.app.core.exception.toAppException
import com.mathgate.app.features.exam.data.local.questions.source.ExamQuestionsLocalSource
import com.mathgate.app.features.exam.data.local.themes.source.ExamThemeLocalSource
import com.mathgate.app.features.exam.data.remote.source.ExamRemoteSource
import com.mathgate.app.features.exam.domain.entity.ExamQuestion
import com.mathgate.app.features.exam.domain.entity.ExamTheme
import com.mathgate.app.shared.exam.entity.ExamTypes
import com.mathgate.app.features.exam.domain.repository.ExamRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExamRepositoryImpl @Inject constructor(
    private val remote: ExamRemoteSource,
    private val localQuestions: ExamQuestionsLocalSource,
    private val localThemes: ExamThemeLocalSource,
    private val ioDispatcher: CoroutineDispatcher
) : ExamRepository {

    val mutex = Mutex()
    override suspend fun getExamQuestion(type: ExamTypes): Flow<AppResult<ExamQuestion>> = flow {
        emit(AppResult.Loading)
        try {
            val response = remote.getExamQuestion(type)
            val (blocks, solBlocks) = coroutineScope {
                val blocksDef = async(ioDispatcher) { remote.downloadImagesForBlocks(response.blocks) }
                val solBlocksDef = async(ioDispatcher) { remote.downloadImagesForBlocks(response.solutionBlocks) }
                blocksDef.await() to solBlocksDef.await()
            }
            val final = response.copy(blocks = blocks, solutionBlocks = solBlocks)
            Log.d("QUESTION", final.toString())
            localQuestions.insert(final, type)
            emit(AppResult.Success(final))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val cached = localQuestions.get(type)
            if (cached != null) {
                localQuestions.invalidate(cached.id)
                emit(AppResult.Success(cached))
            } else {
                emit(
                    AppResult.Error(
                        message = e.toAppException().message,
                        data = null
                    )
                )
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
        } catch (e: CancellationException) {
          throw e
        } catch (e: Exception) {
            emit(
                AppResult.Error(
                    message = e.toAppException().message,
                    data = null
                )
            )
        }

    }

    override suspend fun getExamQuestionByNumber(type: ExamTypes, number: Int): Flow<AppResult<ExamQuestion>> = flow {
        emit(AppResult.Loading)
        try {
            val response = remote.getExamQuestionByNumber(type, number)
            val (blocks, solBlocks) = coroutineScope {
                val blocksDef = async(ioDispatcher) { remote.downloadImagesForBlocks(response.blocks) }
                val solBlocksDef = async(ioDispatcher) { remote.downloadImagesForBlocks(response.solutionBlocks) }
                blocksDef.await() to solBlocksDef.await()
            }
            val final = response.copy(blocks = blocks, solutionBlocks = solBlocks)
            Log.d("QUESTION", final.toString())
            localQuestions.insert(final, type)
            emit(AppResult.Success(final))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val cached = localQuestions.getByNumber(type, number)
            if (cached != null) {
                localQuestions.invalidate(cached.id)
                emit(AppResult.Success(cached))
            } else {
                emit(
                    AppResult.Error(
                        message = e.toAppException().message,
                        data = null
                    )
                )
            }
        }
    }

    override suspend fun preloadQuestions(type: ExamTypes, number: Int?, count: Int) {
        mutex.withLock {
            val semaphore = Semaphore(10)
            supervisorScope {
                val toCache = List(count) {
                    async(ioDispatcher) {
                        semaphore.withPermit {
                            try {
                                val question = if (number != null) remote.getExamQuestionByNumber(
                                    type,
                                    number
                                ) else remote.getExamQuestion(type)
                                val (blocks, solBlocks) = coroutineScope {
                                    val blocksDef = async(ioDispatcher) { remote.downloadImagesForBlocks(question.blocks) }
                                    val solBlocksDef = async(ioDispatcher) { remote.downloadImagesForBlocks(question.solutionBlocks) }
                                    blocksDef.await() to solBlocksDef.await()
                                }
                                question.copy(blocks = blocks, solutionBlocks = solBlocks)
                            } catch (e: CancellationException) {
                                throw e
                            } catch (e: Exception) {
                                Log.w("ExamPreload", "Не удалось предзагрузить вопрос", e)
                                null
                            }
                        }
                    }
                }.awaitAll().filterNotNull()

                localQuestions.insertMore(toCache, type)
            }
        }
    }
}