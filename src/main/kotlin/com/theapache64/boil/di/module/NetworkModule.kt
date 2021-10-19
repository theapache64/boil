package com.theapache64.boil.di.module

import com.github.theapache64.retrosheet.RetrosheetInterceptor
import com.theapache64.boil.data.remote.Api
import com.theapache64.boil.util.calladapter.flow.FlowResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 09:56
 */
@Module
class NetworkModule {
    companion object {
        const val REGISTRY_SHEET_URL =
            "https://docs.google.com/spreadsheets/d/1OF384yi-k3iBgiyLnhYDAoYAV8wJGCh2yEqm3MfQQko/"
    }

    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .addSheet(
                "groups",
                "id", "group_name", "files", "instructions"
            )
            .build()
    }

    @Provides
    fun provideOkHttpClient(retrosheetInterceptor: RetrosheetInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(retrosheetInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(REGISTRY_SHEET_URL)
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }
}