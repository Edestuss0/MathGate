package com.mathgate.app.core.di

import com.mathgate.app.domain.exam.repository.ExamRepositoryImpl
import com.mathgate.app.domain.exam.repository.ExamRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ExamRepositoryBind {
    @Binds @Singleton
    abstract fun bindExamRepository(
        impl: ExamRepositoryImpl
    ): ExamRepository
}