package com.theapache64.boil.feature.add

import kotlinx.coroutines.runBlocking
import picocli.CommandLine
import java.util.concurrent.Callable
import javax.inject.Inject

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 09:26
 */
@CommandLine.Command(
    name = "add",
    description = ["To add boilerplate code group"]
)
class AddView : Callable<Int> {

    @CommandLine.Parameters(index = "0", description = ["Group name"])
    lateinit var groupName: String

    @Inject
    lateinit var addViewModel: AddViewModel

    override fun call(): Int = runBlocking {
        addViewModel.call(this@AddView)
    }
}