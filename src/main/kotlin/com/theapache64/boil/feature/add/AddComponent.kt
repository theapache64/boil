package com.theapache64.boil.feature.add

import dagger.Component

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 10:00
 */
@Component
interface AddComponent {
    fun inject(add: AddView)
}