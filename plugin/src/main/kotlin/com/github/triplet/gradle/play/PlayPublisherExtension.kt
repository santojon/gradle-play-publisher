package com.github.triplet.gradle.play

import com.android.build.gradle.api.ApkVariantOutput
import com.github.triplet.gradle.play.internal.*
import org.gradle.api.Action
import org.gradle.api.tasks.*
import java.io.File
import java.io.Serializable

@Suppress("PropertyName")
open class PlayPublisherExtension @JvmOverloads constructor(
        @get:Internal internal val name: String = "default" // Needed for Gradle
) : Serializable {
    @get:Internal("Backing property for public input")
    internal var _isEnabled: Boolean? = null
    /**
     * Enables or disables GPP.
     *
     * Defaults to `true`.
     */
    @get:Input
    var isEnabled
        get() = _isEnabled ?: true
        set(value) {
            _isEnabled = value
        }

    @get:Internal("Backing property for public input")
    internal var _serviceAccountCredentials: File? = null
    /**
     * Service Account authentication file. Json is preferred, but PKCS12 is also supported. For
     * PKCS12 to work, the [serviceAccountEmail] must be specified.
     */
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:InputFile
    var serviceAccountCredentials
        get() = _serviceAccountCredentials
        set(value) {
            _serviceAccountCredentials = value
        }

    @get:Internal("Backing property for public input")
    internal var _serviceAccountEmail: String? = null
    /** Service Account email. Only needed if PKCS12 credentials are used. */
    @get:Optional
    @get:Input
    var serviceAccountEmail
        get() = _serviceAccountEmail
        set(value) {
            _serviceAccountEmail = value
        }

    @get:Internal("Backing property for public input")
    internal var _defaultToAppBundles: Boolean? = null
    /**
     * Choose the default packaging method. Either App Bundles or APKs. Affects tasks like
     * `publish`.
     *
     * Defaults to `false` because App Bundles require Google Play App Signing to be configured.
     */
    @get:Input
    var defaultToAppBundles
        get() = _defaultToAppBundles ?: false
        set(value) {
            _defaultToAppBundles = value
        }

    @get:Internal("Backing property for public input")
    internal var _commit: Boolean? = null
    /**
     * Choose whether or not to apply the changes from this build. Defaults to true.
     */
    @get:Input
    var commit
        get() = _commit ?: true
        set(value) {
            _commit = value
        }

    @get:Internal("Backing property for public input")
    internal var _fromTrack: String? = null
    /**
     * Specify the track from which to promote a release. That is, the specified track will be
     * promoted to [track].
     *
     * See [track] for valid values. The default is determined dynamically from the most unstable
     * release available for promotion. That is, if there is a stable release and an alpha release,
     * the alpha will be chosen.
     */
    @get:Input
    var fromTrack
        get() = _fromTrack ?: track
        set(value) {
            _fromTrack = value
        }

    @get:Internal("Backing property for public input")
    internal var _track: String? = null
    /**
     * Specify the track in which to upload your app.
     *
     * May be one of `internal`, `alpha`, `beta`, `production`, or a custom track. Defaults to
     * `internal`.
     */
    @get:Input
    var track
        get() = trackOrDefault
        set(value) {
            _track = value
        }

    @get:Internal("Backing property for public input")
    internal var _userFraction: Double? = null
    /**
     * Specify the initial user fraction intended to receive an `inProgress` release. Defaults to
     * 0.1 (10%).
     *
     * @see releaseStatus
     */
    @get:Input
    var userFraction: Double
        get() = _userFraction ?: 0.1
        set(value) {
            _userFraction = value
        }

    @get:Internal("Backing property for public input")
    internal var _trackObb: String? = null
    /**
     * Specify the obb track.
     */
    @get:Input
    var trackObb
        get() = trackObbOrDefault
        set(value) {
            _trackObb = value
        }

    @get:Internal("Backing property for public input")
    internal var _attachObb: Int? = null
    /**
     * Specify the obb APK version code to attach to it.
     */
    @get:Input
    var attachObb
        get() = _attachObb ?: -1
        set(value) {
            _attachObb = value
        }

    @get:Internal("Backing property for public input")
    internal var _resolutionStrategy: ResolutionStrategy? = null
    /**
     * Specify the resolution strategy to employ when a version conflict occurs.
     *
     * May be one of `auto`, `fail`, or `ignore`. Defaults to `fail`.
     */
    @get:Input
    var resolutionStrategy
        get() = resolutionStrategyOrDefault.publishedName
        set(value) {
            _resolutionStrategy = requireNotNull(
                    ResolutionStrategy.values().find { it.publishedName == value }
            ) {
                "Resolution strategy must be one of " +
                        ResolutionStrategy.values().joinToString { "'${it.publishedName}'" }
            }
        }

    @get:Internal("ProcessArtifactMetadata is always out-of-date. Also, Closures with " +
            "parameters cannot be used as inputs.")
    internal var _outputProcessor: Action<ApkVariantOutput>? = null

    /**
     * If the [resolutionStrategy] is auto, provide extra processing on top of what this plugin
     * already does. For example, you could update each output's version name using the newly
     * mutated version codes.
     *
     * Note: by the time the output is received, its version code will have been linearly shifted
     * such that the smallest output's version code is 1 unit greater than the maximum version code
     * found in the Play Store.
     */
    @Suppress("unused") // Public API
    fun outputProcessor(processor: Action<ApkVariantOutput>) {
        _outputProcessor = processor
    }

    @get:Internal("Backing property for public input")
    internal var _releaseStatus: ReleaseStatus? = null
    /**
     * Specify the status to apply to the uploaded app release.
     *
     * May be one of `completed`, `draft`, `halted`, or `inProgress`. Defaults to `completed`.
     */
    @get:Input
    var releaseStatus: String
        get() = releaseStatusOrDefault.publishedName
        set(value) {
            _releaseStatus = requireNotNull(
                    ReleaseStatus.values().find { it.publishedName == value }
            ) {
                "Release Status must be one of " +
                        ReleaseStatus.values().joinToString { "'${it.publishedName}'" }
            }
        }

    @get:Internal("Backing property for public input")
    internal var _artifactDir: File? = null
    /**
     * Specify a directory where prebuilt artifacts such as APKs or App Bundles may be found. The
     * directory must exist and should contain only artifacts intended to be uploaded. If no
     * directory is specified, your app will be built on-the-fly when you try to publish it.
     *
     * Defaults to null (i.e. your app will be built pre-publish).
     */
    @get:Internal("Directory mapped to a useful set of files later")
    var artifactDir: File?
        get() = _artifactDir
        set(value) {
            _artifactDir = value
        }

    internal fun toSerializable() = Serializable().also {
        it._isEnabled = _isEnabled
        it._serviceAccountCredentials = _serviceAccountCredentials
        it._serviceAccountEmail = _serviceAccountEmail
        it._defaultToAppBundles = _defaultToAppBundles
        it._commit = _commit
        it._fromTrack = _fromTrack
        it._track = _track
        it._userFraction = _userFraction
        it._resolutionStrategy = _resolutionStrategy
        it._releaseStatus = _releaseStatus
        it._artifactDir = _artifactDir
        it._attachObb = attachObb
        it._trackObb = trackObb
    }

    internal class Serializable : PlayPublisherExtension()
}
