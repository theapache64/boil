package com.theapache64.boil.feature.registry

import com.theapache64.boil.base.BaseViewModel
import com.theapache64.boil.di.module.NetworkModule
import java.awt.Desktop
import java.net.URI
import javax.inject.Inject

class RegistryViewModel @Inject constructor() : BaseViewModel<RegistryView>() {
    override suspend fun call(command: RegistryView): Int {
        Desktop.getDesktop().browse(URI(NetworkModule.REGISTRY_SHEET_URL))
        return 0
    }
}