package app.template.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    const val GBOARD_PACKAGE_NAME = "com.google.android.inputmethod.latin"

    val COMPATIBILITY_GBOARD_18_5_4 = Compatibility(
        name = "Gboard",
        packageName = GBOARD_PACKAGE_NAME,
        apkFileType = ApkFileType.APK,
        appIconColor = 0x1A73E8,
        targets = listOf(
            AppTarget(version = "18.5.4.993608717-release-arm64-v8a")
        )
    )

    // Kept for the template's example patch so the starter source remains compilable.
    val COMPATIBILITY_EXAMPLE = COMPATIBILITY_GBOARD_18_5_4
}
