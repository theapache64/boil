package com.theapache64.boil.di.module

import com.theapache64.boil.data.remote.Api
import com.theapache64.boil.utils.calladapter.flow.FlowResourceCallAdapterFactory
import com.theapache64.retrosheet.RetrosheetInterceptor
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

    @Provides
    fun provideRetrosheetInterceptor(): RetrosheetInterceptor {
        return RetrosheetInterceptor.Builder()
            .addSheet(
                "groups",
                "id", "group_name", "classes", "instructions"
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
            .baseUrl("https://docs.google.com/spreadsheets/d/1OF384yi-k3iBgiyLnhYDAoYAV8wJGCh2yEqm3MfQQko/")
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