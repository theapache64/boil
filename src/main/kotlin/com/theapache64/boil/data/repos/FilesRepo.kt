package com.theapache64.boil.data.repos

import com.theapache64.boil.data.remote.Api
import com.theapache64.boil.util.calladapter.flow.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 12:49
 */
class FilesRepo @Inject constructor(
    private val api: Api
) {

    companion object {
        private const val SOURCE_BASE = "https://raw.githubusercontent.com/theapache64/boil/master/files/"
    }

    fun getFile(fileName: String): Flow<Resource<String>> {
        val fullUrl = "$SOURCE_BASE$fileName"
        return api.getFile(fullUrl)
    }

    suspend fun downloadFile(fileName: String): Response<ResponseBody> {
        val fullUrl = "$SOURCE_BASE$fileName"
        return api.downloadFile(fullUrl)
    }
}