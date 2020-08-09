package com.theapache64.boil.data.repos

import com.theapache64.boil.data.remote.Api
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

    suspend fun getFile(fileName: String) {
        val fullUrl = "$SOURCE_BASE$fileName"
        println(fullUrl)
        val fileContent = api.getFile(fullUrl)
        println(fileContent)
    }
}