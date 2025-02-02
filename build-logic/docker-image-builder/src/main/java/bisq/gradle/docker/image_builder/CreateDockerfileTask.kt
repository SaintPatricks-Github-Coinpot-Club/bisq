package bisq.gradle.docker.image_builder

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class CreateDockerfileTask : DefaultTask() {

    @get:Input
    abstract val archiveFileName: Property<String>

    @get:Input
    abstract val classpathFileNames: ListProperty<String>

    @get:Input
    abstract val mainClassName: Property<String>

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun create() {
        val classpath = classpathFileNames.get()
            .joinToString(":") { "/seednode/lib/$it" }

        val dockerFileContent: String = readDockerFileTemplate()
            .replace("'{{ ARCHIVE_PATH }}'", archiveFileName.get())
            .replace("'{{ CLASS_PATH }}'", classpath)
            .replace("'{{ MAIN_CLASS }}'", mainClassName.get())

        outputFile.asFile.get()
            .writeText(dockerFileContent)
    }

    private fun readDockerFileTemplate(): String {
        this.javaClass.getResourceAsStream("/Dockerfile")
            .use { inputStream ->
                inputStream!!.bufferedReader()
                    .use { bufferedReader ->
                        return bufferedReader.readText()
                    }
            }
    }

}
