package com.mathgate.app.core.di

import com.mathgate.app.domain.freemode.repository.FreemodeRepositoryImpl
import com.mathgate.app.domain.freemode.repository.IFreemodeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FreemodeRepositoryBind {
    @Binds @Singleton
    abstract fun bindFreemodeRepository(
        impl: FreemodeRepositoryImpl
    ): IFreemodeRepository
}