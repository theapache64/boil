package com.theapache64.boil.di.module

import dagger.Module
import dagger.Provides
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 09:56
 */
@Singleton
@Module
object NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}