package com.theapache64.boil.data.remote

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 11:14
 */
@JsonClass(generateAdapter = true)
data class Group(
    @Json(name = "instructions")
    val instructions: String,
    @Json(name = "files")
    val files: String, // SingleLiveEvent.kt
    @Json(name = "group_name")
    val groupName: String, // Group Name
    @Json(name = "id")
    val id: Int // 1
) {
    @Transient
    val classList = files.split("\n")
}