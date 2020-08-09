package com.theapache64.boil.feature.add

import com.theapache64.boil.base.BaseViewModel
import com.theapache64.boil.data.repos.RegistryRepo
import javax.inject.Inject

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 10:06
 */
class AddViewModel @Inject constructor(
    private val registryRepo: RegistryRepo
) : BaseViewModel<AddView>() {
    override suspend fun call(command: AddView): Int {
        TODO("Not yet implemented")
    }
}