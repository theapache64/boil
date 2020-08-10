package com.theapache64.boil.feature.add

import com.theapache64.boil.base.BaseViewModel
import com.theapache64.boil.data.repos.FilesRepo
import com.theapache64.boil.data.repos.RegistryRepo
import com.theapache64.boil.utils.GradleUtils
import com.theapache64.boil.utils.InputUtils
import com.theapache64.boil.utils.calladapter.flow.Resource
import kotlinx.coroutines.flow.collect
import java.io.File
import javax.inject.Inject
import kotlin.system.exitProcess

/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 10:06
 */
class AddViewModel @Inject constructor(
    private val registryRepo: RegistryRepo,
    private val filesRepo: FilesRepo
) : BaseViewModel<AddView>() {

    companion object {
        private const val ENV_VAR_PACKAGE_NAME = "${'$'}PACKAGE_NAME"
    }

    override suspend fun call(command: AddView): Int {

        // Search for given keyword in registry
        val group = registryRepo.getGroup(command.groupName)
        group.collect { it ->
            when (it) {
                is Resource.Loading -> {
                    println("🔍 Searching for group '${command.groupName}'")
                }
                is Resource.Success -> {
                    println("👌 Found '${command.groupName}'")
                    val classList = it.data.classList
                    if (classList.isNotEmpty()) {

                        classList.forEach {
                            println("    -> $it")
                        }

                        val shouldStartIntegration = true || InputUtils.getString(
                            "⌨ Type 'y' to start integration",
                            true
                        ).trim().toLowerCase() == "y"

                        if (shouldStartIntegration) {
                            startIntegration(classList)
                        } else {
                            println("❌ Integration cancelled")
                        }

                    } else {
                        println("🤷‍♂ ️But there are no classes connected to it.")
                    }
                }
                is Resource.Error -> TODO()
            }
        }

        return 0
    }

    private suspend fun startIntegration(classList: List<String>) {
        println("⬇️ Downloading files")
        val files = downloadFiles(classList)
        require(files.size == classList.size) { "File count mismatch" }
        onFilesDownloaded(files)
    }

    private fun onFilesDownloaded(files: Map<String, String>) {
        println("👌 All files downloaded. Initializing integration...")
        val currentDir = System.getProperty("user.dir")

        // Getting param values
        val packageNamePair = GradleUtils.getProjectPackageName(currentDir) ?: throw IllegalArgumentException(
            """
            Failed to get package name from $currentDir
        """.trimIndent()
        )
        val dirName = packageNamePair.first
        val packageName = packageNamePair.second
        println("Package name is $packageNamePair")

        // Setting param values
        for (file in files) {
            val fileName = file.key
            val fileContent = file.value
            val updatedFileContent = fileContent.replace(ENV_VAR_PACKAGE_NAME, packageName)
            val firstLine = fileContent.lines().first()
            require(firstLine.startsWith("package")) { "Invalid file header. All file should start with 'package'. Found '$firstLine'" }
            val fullPackageName = updatedFileContent.lines()[0].split("package")[1].trim()
            val fileTargetDirPath =
                "${System.getProperty("user.dir")}${File.separator}$dirName${fullPackageName.replace('.', '/')}"
            val fileTargetDir = File(fileTargetDirPath)
            if (!fileTargetDir.exists()) {
                fileTargetDir.mkdirs()
            }
            val targetFile = File(fileTargetDirPath + File.separator + fileName)
            if (targetFile.exists()) {
                println("${targetFile.absolutePath} exists!")
            } else {
                targetFile.writeText(updatedFileContent)
                println("Created ${targetFile.absolutePath}")
            }
        }
    }

    private suspend fun downloadFiles(classList: List<String>): Map<String, String> {
        val fileContent = mutableMapOf<String, String>()
        for (fileName in classList) {
            filesRepo.getFile(fileName).collect {
                when (it) {
                    is Resource.Loading -> {
                        println("⬇️ Getting file '$fileName'...")
                    }

                    is Resource.Success -> {
                        println("⬇️ File downloaded '$fileName'")
                        fileContent[fileName] = it.data
                    }

                    is Resource.Error -> {
                        println("❌ Failed to get file '$fileName' Due to '${it.errorData}'. Cancelling integration...")
                        exitProcess(0)
                    }
                }

            }
        }
        return fileContent
    }
}