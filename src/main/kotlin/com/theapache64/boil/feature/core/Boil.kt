package com.theapache64.boil.feature.core

import com.theapache64.boil.feature.add.AddView
import picocli.CommandLine
import java.util.concurrent.Callable

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 09:51
 */
@CommandLine.Command(
    name = "boil",
    version = ["v1.0.0-alpha05"],
    mixinStandardHelpOptions = true,
    subcommands = [
        AddView::class
    ]
)
class Boil : Callable<Int> {
    override fun call(): Int {
        return 0
    }
}