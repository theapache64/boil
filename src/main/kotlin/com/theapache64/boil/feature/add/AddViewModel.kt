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
                    println("👌 Found '${command.groupName}'. Below given files connected")
                    val classList = it.data.classList
                    if (classList.isNotEmpty()) {

                        classList.forEach {
                            println("    -> $it")
                        }

                        val shouldStartIntegration = InputUtils.getString(
                            "⌨ Type 'y' to start integration",
                            true
                        ).trim().toLowerCase() == "y"

                        if (shouldStartIntegration) {
                            startIntegration(classList)

                            // Printing instructions
                            val instructions = it.data.instructions
                            if (instructions.isNotEmpty()) {
                                println("\nInstructions:\n$instructions")
                            }
                        } else {
                            println("❌ Integration cancelled")
                        }

                    } else {
                        println("🤷‍♂ ️But there are no classes connected to it.")
                    }
                }
                is Resource.Error -> {
                    println("Error : ${it.errorData}")
                }
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

        // Setting param values
        for (file in files) {
            var fileName = file.key
            val fileContent = file.value
            val updatedFileContent = fileContent.replace(ENV_VAR_PACKAGE_NAME, packageName)


            val fileTargetDirPath = when (val fileExt = File(fileName).extension) {
                "kt", "java" -> {
                    val firstLine = fileContent.lines().first()
                    require(firstLine.startsWith("package")) { "Invalid file header. All file should start with 'package'. Found '$firstLine'" }
                    val fullPackageName = updatedFileContent.lines()[0].split("package")[1].trim()
                    "${System.getProperty("user.dir")}${File.separator}$dirName${fullPackageName.replace('.', '/')}"
                }

                "xml" -> {
                    require(fileName.contains("-")) { "XML file name should contain '-' to sep target res directory" }
                    val fs = fileName.split("-")
                    val subDirName = fs[0]
                    fileName = fs[1]
                    "${System.getProperty("user.dir")}/app/src/main/res/$subDirName"
                }
                else -> throw IllegalArgumentException("Unknown file type '$fileExt'!")
            }
            val fileTargetDir = File(fileTargetDirPath)
            if (!fileTargetDir.exists()) {
                fileTargetDir.mkdirs()
            }
            val targetFile = File(fileTargetDirPath + File.separator + fileName)
            if (targetFile.exists()) {
                println("${targetFile.absolutePath} exists!")
            } else {
                targetFile.writeText(updatedFileContent)
                println("➡️ Created ${targetFile.absolutePath}")
            }
        }

        println("👍 Integration finished")
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