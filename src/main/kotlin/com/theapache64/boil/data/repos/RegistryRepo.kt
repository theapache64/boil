package com.theapache64.boil.data.repos

import com.theapache64.boil.data.remote.Api
import javax.inject.Inject

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 09:55
 */
class RegistryRepo @Inject constructor(
    private val api: Api
) {
    fun getGroup(groupName: String) = api.getGroup(groupName)
}