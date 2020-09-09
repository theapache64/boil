package com.theapache64.boil.data.remote

import com.theapache64.boil.utils.calladapter.flow.Resource
import com.theapache64.retrosheet.core.Read
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 10:17
 */
interface Api {

    @Read("SELECT * WHERE group_name contains :groupName")
    @GET("groups")
    fun getGroup(
        @Query("groupName") groupName: String
    ): Flow<Resource<Group>>

    @GET
    fun getFile(
        @Url url: String
    ): Flow<Resource<String>>

    @Streaming
    @GET
    suspend fun downloadFile(
        @Url url: String
    ): Response<ResponseBody>
}