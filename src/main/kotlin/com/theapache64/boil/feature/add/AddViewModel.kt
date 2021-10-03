package com.theapache64.boil.feature.add

import com.theapache64.boil.base.BaseViewModel
import com.theapache64.boil.data.repos.FilesRepo
import com.theapache64.boil.data.repos.RegistryRepo
import com.theapache64.boil.util.GradleUtils
import com.theapache64.boil.util.InputUtils
import com.theapache64.boil.util.calladapter.flow.Resource
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
        private val KOTLIN_SIGNATURE by lazy {
            """/*
 * Copyright 2021 Boil (https://github.com/theapache64/boil)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */""".trimIndent()
        }
        private const val ENV_VAR_PACKAGE_NAME = "${'$'}PACKAGE_NAME"

        private val SUPPORTED_TEXT_FILES = arrayOf(
            "kt",
            "java",
            "txt",
            "xml",
            "json"
        )
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
                    val isExactGroupName = it.data.groupName == command.groupName

                    if (isExactGroupName) {
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
                    } else {
                        val userResp = InputUtils.getString(
                            "🤷‍♂ Couldn't find any group with '${command.groupName}'. Did you mean '${it.data.groupName}'? (y/n)",
                            false
                        )

                        if (userResp.equals("y", ignoreCase = true) || userResp.equals("yes", ignoreCase = true)) {
                            // Retry with suggestion
                            val request = AddView().apply {
                                groupName = it.data.groupName
                            }
                            call(
                                request
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    println("Error : ${it.errorData}")
                }
            }
        }

        return 0
    }

    private suspend fun startIntegration(fileList: List<String>) {
        println("⬇️ Downloading files")
        val files = downloadFiles(fileList)
        require(files.size == fileList.size) { "File count mismatch" }
        onFilesDownloaded(files)
    }

    private fun onFilesDownloaded(files: Map<String, String>) {
        println("👌 All files downloaded. Initializing integration...")
        val currentDir = System.getProperty("user.dir")


        // Setting param values
        for (file in files) {

            // Getting param values
            val packageNamePair = GradleUtils.getProjectPackageName(file.key, currentDir) ?: throw IllegalArgumentException(
                """
            Failed to get package name from $currentDir
        """.trimIndent()
            )
            val (dirName, packageName) = packageNamePair

            var fileName = file.key
            val fileContent = file.value
            val fileExt = File(fileName).extension
            val updatedFileContent = fileContent.replace(ENV_VAR_PACKAGE_NAME, packageName)

            if (SUPPORTED_TEXT_FILES.contains(fileExt)) {
                // text file
                val fileTargetDirPath = when (fileExt) {
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
                    val signaturedFileContent = addSignature(updatedFileContent, fileExt)
                    targetFile.writeText(signaturedFileContent)
                    println("➡️ Created ${targetFile.absolutePath}")
                }

            } else {
                // raw file
                println("➡️ Created $fileContent")
            }

        }

        println("👍 Integration finished")
    }

    private fun addSignature(fileContent: String, fileExt: String): String {
        return when (fileExt) {
            "kt" -> {
                "${KOTLIN_SIGNATURE}\n$fileContent"
            }
            else -> fileContent
        }
    }

    private suspend fun downloadFiles(classList: List<String>): Map<String, String> {
        val fileContent = mutableMapOf<String, String>()

        for (fileName in classList) {

            val extension = File(fileName).extension


            if (SUPPORTED_TEXT_FILES.contains(extension)) {
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
            } else {
                println("Downloading RAW file...")
                filesRepo.downloadFile(fileName).let {
                    if (it.code() == 200) {
                        val targetDir = when (extension) {
                            "ttf", "otf" -> {
                                "${System.getProperty("user.dir")}/app/src/main/res/font"
                            }
                            else -> throw IllegalArgumentException("Unknown RAW file type '$extension'!")
                        }
                        val file = File("$targetDir/$fileName")
                        if (file.exists()) {
                            file.delete()
                        }
                        file.parentFile.mkdirs()
                        @Suppress("BlockingMethodInNonBlockingContext")
                        file.writeBytes(it.body()!!.bytes())
                        fileContent[fileName] = file.absolutePath
                    }
                }
            }
        }
        return fileContent
    }
}