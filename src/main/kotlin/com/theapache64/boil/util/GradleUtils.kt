package com.theapache64.boil.util

import java.io.File
import java.io.FileNotFoundException


/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 13:32
 */
object GradleUtils {

    private const val START_DIR_ANDROID = "app/src/main/java/"
    private const val START_DIR_GRADLE = "src/main/kotlin/"
    private const val START_DIR_TEST = "app/src/test/java/"
    private const val START_DIR_ANDROID_TEST = "app/src/androidTest/java/"

    fun getProjectPackageName(fileName: String, projectFolder: String): Pair<String, String>? {
        var gradleFile = File("$projectFolder/app/build.gradle")

        if (!gradleFile.exists()) {
            // not an android project. so provide support for java gradle project
            gradleFile = File("$projectFolder/build.gradle")

            if (!gradleFile.exists()) {
                // # its not java gradle project, lets try kts gradle file
                gradleFile = File("$projectFolder/build.gradle.kts")
            }
        }

        println("Gradle file is ${gradleFile.absolutePath}")

        return try {
            val startDir = getStartDir()
            println("StartDir is '$startDir'")
            val dirName = if (startDir == START_DIR_ANDROID && fileName.contains("-")) {
                when (val targetDir = fileName.split("-")[0]) {
                    "test" -> START_DIR_TEST
                    "androidTest" -> START_DIR_ANDROID_TEST
                    else -> error("Undefined targetDir '$targetDir'")
                }
            } else {
                getStartDir()
            }


            // Try getting it from directory structure
            val loopFromDir = File(projectFolder + File.separator + startDir)
            val loopEndDir = getLoopEndDir(loopFromDir).absolutePath
            val i1 = loopEndDir.indexOf(startDir) + startDir.length
            val topPackageName = loopEndDir.substring(i1).replace('/', '.')

            Pair(dirName, topPackageName)
        } catch (e: FileNotFoundException) {
            return null
        }
    }

    private fun getStartDir(): String {
        return if (START_DIR_ANDROID.toFile().exists()) {
            START_DIR_ANDROID
        } else {
            START_DIR_GRADLE
        }
    }

    private fun getLoopEndDir(dir: File): File {
        val child = dir.listFiles()!!
        if (child.size == 1 && child[0].isDirectory) {
            return getLoopEndDir(child[0])
        }
        return dir
    }
}

private fun String.toFile(): File {
    return File(this)
}
