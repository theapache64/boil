package com.theapache64.boil.utils

import java.io.*
import java.util.regex.Pattern


/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 13:32
 */
object GradleUtils {

    private val PACKAGE_REGEX by lazy {
        Pattern.compile("^applicationId \"(.+)\"$")
    }

    private const val START_DIR_ANDROID = "src/main/java/"
    private const val START_DIR_GRADLE = "src/main/kotlin/"

    fun getProjectPackageName(projectFolder: String): Pair<String, String>? {
        var gradleFile = File("$projectFolder/app/build.gradle")
        if (!gradleFile.exists()) {
            // not an android project. so provide support for java gradle project
            gradleFile = File("$projectFolder/build.gradle")
        }
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
                            dirName = START_DIR_ANDROID
                            break
                        }
                    }
                }
            }

            if (packageName == null) {
                // Try getting it from directory structure
                val loopFromDir = File(START_DIR_GRADLE)
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