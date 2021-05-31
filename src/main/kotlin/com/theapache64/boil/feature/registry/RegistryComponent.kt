package com.theapache64.boil.feature.registry

import dagger.Component
import javax.inject.Singleton

@Component
@Singleton
interface RegistryComponent {
    fun inject(registryView: RegistryView)
}