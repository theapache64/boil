package com.theapache64.boil.feature.registry

import kotlinx.coroutines.runBlocking
import picocli.CommandLine
import java.util.concurrent.Callable
import javax.inject.Inject

@CommandLine.Command(
    name = "open",
    description = ["To open the public registry in default browser"]
)
class RegistryView : Callable<Int> {

    @Inject
    lateinit var registryViewModel: RegistryViewModel

    init {
        DaggerRegistryComponent
            .builder()
            .build()
            .inject(this)
    }

    override fun call(): Int = runBlocking {
        registryViewModel.call(this@RegistryView)
    }
}