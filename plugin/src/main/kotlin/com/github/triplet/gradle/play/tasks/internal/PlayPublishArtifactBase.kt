package com.github.triplet.gradle.play.tasks.internal

import com.android.build.gradle.api.ApplicationVariant
import com.github.triplet.gradle.play.PlayPublisherExtension
import com.github.triplet.gradle.play.internal.RELEASE_NAMES_PATH
import com.github.triplet.gradle.play.internal.RELEASE_NOTES_PATH
import com.github.triplet.gradle.play.internal.orNull
import org.gradle.api.tasks.*
import java.io.File

abstract class PlayPublishArtifactBase(
        extension: PlayPublisherExtension,
        variant: ApplicationVariant,
        optionsHolder: TransientTrackOptions.Holder
) : PlayPublishTaskBase(extension, variant), TransientTrackOptions by optionsHolder {
    @get:Internal internal lateinit var resDir: File

    @Suppress("MemberVisibilityCanBePrivate", "unused") // Used by Gradle
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:Optional
    @get:InputDirectory
    internal val releaseNotesDir
        get() = File(resDir, RELEASE_NOTES_PATH).orNull()

    @Suppress("MemberVisibilityCanBePrivate", "unused") // Used by Gradle
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:Optional
    @get:InputDirectory
    internal val consoleNamesDir
        get() = File(resDir, RELEASE_NAMES_PATH).orNull()

    @Suppress("MemberVisibilityCanBePrivate", "unused") // Used by Gradle
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:Optional
    @get:InputFile
    internal val mappingFile: File?
        get() {
            val customDir = extension._artifactDir

            return if (customDir == null) {
                variant.mappingFile?.orNull()
            } else {
                customDir.listFiles().orEmpty().singleOrNull { it.name == "mapping.txt" }
            }
        }
}
