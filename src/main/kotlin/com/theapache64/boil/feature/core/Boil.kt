package com.theapache64.boil.feature.core

import com.theapache64.boil.feature.add.AddView
import com.theapache64.boil.feature.registry.RegistryView
import picocli.CommandLine
import java.util.concurrent.Callable

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 09:51
 */
@CommandLine.Command(
    name = "boil",
    version = ["v1.0.2"],
    mixinStandardHelpOptions = true,
    subcommands = [
        AddView::class,
        RegistryView::class
    ]
)
class Boil : Callable<Int> {
    override fun call(): Int {
        return 0
    }
}