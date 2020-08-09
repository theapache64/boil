package com.theapache64.boil.feature.add

import com.theapache64.boil.base.BaseViewModel
import com.theapache64.boil.data.repos.FilesRepo
import com.theapache64.boil.data.repos.RegistryRepo
import com.theapache64.boil.utils.InputUtils
import com.theapache64.boil.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 10:06
 */
class AddViewModel @Inject constructor(
    private val registryRepo: RegistryRepo,
    private val filesRepo: FilesRepo
) : BaseViewModel<AddView>() {


    override suspend fun call(command: AddView): Int {

        // Search for given keyword in registry
        println("Group name is ${command.groupName}")
        val group = registryRepo.getGroup(command.groupName)
        group.collect { it ->
            when (it) {
                is Resource.Loading -> {
                    println("Searching for group '${command.groupName}'")
                }
                is Resource.Success -> {
                    println("Found '${command.groupName}'")
                    val classList = it.data.classList
                    if (classList.isNotEmpty()) {

                        classList.forEach {
                            println("    -> $it")
                        }

                        val shouldStartIntegration = true || InputUtils.getString(
                            "Type 'y' to start integration",
                            true
                        ).trim().toLowerCase() == "y"

                        if (shouldStartIntegration) {
                            startIntegration(classList)
                        } else {
                            println("Integration cancelled")
                        }

                    } else {
                        println("But there are no classes connected to it.")
                    }
                }
                is Resource.Error -> TODO()
            }
        }

        return 0
    }

    private suspend fun startIntegration(classList: List<String>) {
        println("Starting integration")
        for (fileName in classList) {
            println("File name : $fileName")
            filesRepo.getFile(fileName)
        }
    }
}