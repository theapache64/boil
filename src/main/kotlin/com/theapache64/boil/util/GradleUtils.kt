package com.theapache64.boil.util

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.regex.Pattern


/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 13:32
 */
object GradleUtils {

    private val PACKAGE_REGEX by lazy {
        Pattern.compile("^applicationId \"(.+)\"$")
    }

    private const val START_DIR_ANDROID = "app/src/main/java/"
    private const val START_DIR_GRADLE = "src/main/kotlin/"
    private const val START_DIR_TEST = "app/src/test/java/"
    private const val START_DIR_ANDROID_TEST = "app/src/androidTest/java/"

    /**
     * TODO: Needs to improve package collection algorithm.
     */
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
            var packageName: String? = null

            //Reading gradle file to find package name
            var dirName: String? = null
            gradleFile.readLines().let { lines ->
                for (line in lines) {
                    if (line.contains("applicationId")) {
                        val matcher = PACKAGE_REGEX.matcher(line.trim())
                        if (matcher.find()) {
                            packageName = matcher.group(1)
                            dirName = if (fileName.contains("-")) {
                                when (val targetDir = fileName.split("-")[0]) {
                                    "test" -> START_DIR_TEST
                                    "androidTest" -> START_DIR_ANDROID_TEST
                                    else -> error("Undefined targetDir '$targetDir'")
                                }
                            } else {
                                START_DIR_ANDROID
                            }
                            break
                        }
                    }
                }
            }

            if (packageName == null) {
                // Try getting it from directory structure
                val loopFromDir = File(projectFolder + File.separator + START_DIR_GRADLE)
                val loopEndDir = getLoopEndDir(loopFromDir).absolutePath
                val i1 = loopEndDir.indexOf(START_DIR_GRADLE) + START_DIR_GRADLE.length
                packageName = loopEndDir.substring(i1).replace('/', '.')
                dirName = START_DIR_GRADLE
            }

            if (packageName == null) {
                throw IOException("Unable to find package name from " + gradleFile.absolutePath)
            }

            Pair(dirName!!, packageName!!)
        } catch (e: FileNotFoundException) {
            return null
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