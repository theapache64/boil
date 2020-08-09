package com.theapache64.boil

import com.theapache64.boil.feature.core.Boil
import picocli.CommandLine
import kotlin.system.exitProcess

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 09:13
 */
fun main(args: Array<String>) {
    val exitCode = CommandLine(Boil()).execute(*args)
    exitProcess(exitCode)
}