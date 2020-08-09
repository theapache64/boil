package com.theapache64.boil.utils

import java.io.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Created by theapache64 : Aug 09 Sun,2020 @ 13:32
 */
object GradleUtils {

    private val PACKAGE_REGEX by lazy {
        Pattern.compile("^applicationId \"(.+)\"$")
    }

    private const val START_DIR = "src/main/kotlin/"

    fun getProjectPackageName(projectFolder: String): String? {
        var gradleFile = File("$projectFolder/app/build.gradle")
        if (!gradleFile.exists()) {
            // not an android project. so provide support for java gradle project
            gradleFile = File("$projectFolder/build.gradle")
        }
        return try {
            var packageName: String? = null

            //Reading gradle file to find package name
            gradleFile.readLines().let { lines ->
                for (line in lines) {
                    if (line.contains("applicationId")) {
                        val matcher = PACKAGE_REGEX.matcher(line.trim())
                        if (matcher.find()) {
                            packageName = matcher.group(1)
                            break
                        }
                    }
                }
            }

            if (packageName == null) {
                // Try getting it from directory structure
                val loopFromDir = File(START_DIR)
                val loopEndDir = getLoopEndDir(loopFromDir).absolutePath
                val i1 = loopEndDir.indexOf(START_DIR) + START_DIR.length
                packageName = loopEndDir.substring(i1).replace('/', '.')
            }


            if (packageName == null) {
                throw IOException("Unable to find package name from " + gradleFile.absolutePath)
            }
            packageName
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