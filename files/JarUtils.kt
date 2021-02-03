package $PACKAGE_NAME.utils

import $PACKAGE_NAME.Main
import java.io.File

object JarUtils {

    fun getJarDir(): String {

        val jarDir = File(
            Main::class.java.protectionDomain.codeSource.location
                .toURI()
        ).parent

        if (jarDir.contains("/out/production") || jarDir.contains("/build/classes/")) {
            return ""
        }

        return "$jarDir/"
    }
}
