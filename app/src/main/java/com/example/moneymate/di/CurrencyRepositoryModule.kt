package com.example.moneymate.di

import com.example.moneymate.data.DefaultCurrencyRepository
import com.example.moneymate.domain.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CurrencyRepositoryModule {

    @Binds
    fun bindCurrencyRepository(
        defaultCurrencyRepository: DefaultCurrencyRepository
    ) : CurrencyRepository

}