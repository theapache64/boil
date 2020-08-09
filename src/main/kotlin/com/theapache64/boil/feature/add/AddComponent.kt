package com.theapache64.boil.feature.add

import com.theapache64.boil.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 10:00
 */
@Component(
    modules = [
        NetworkModule::class
    ]
)
@Singleton
interface AddComponent {
    fun inject(add: AddView)
}