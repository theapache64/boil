import java.io.File
import java.util.zip.ZipFile

fun File.unzip(
    outputDir: File = getDefaultOutputDir(this)
): File {
    // Delete existing first
    outputDir.deleteRecursively()

    ZipFile(this).use { zip ->
        zip.entries().asSequence().forEach { entry ->
            if (!entry.isDirectory) {
                zip.getInputStream(entry).use { input ->
                    val outputFile = File("${outputDir.absolutePath}${File.separator}${entry.name}")

                    with(outputFile) {
                        if (!outputFile.parentFile.exists()) {
                            parentFile.mkdirs()
                        }
                    }

                    outputFile.createNewFile()
                    outputFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }
    }

    return outputDir
}

private fun getDefaultOutputDir(inputZipFile: File): File {
    return File("${inputZipFile.parentFile.absolutePath}${File.separator}${inputZipFile.nameWithoutExtension}")
}
