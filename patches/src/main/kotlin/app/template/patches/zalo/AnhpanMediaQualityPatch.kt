package app.template.patches.zalo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.string
import com.android.tools.smali.dexlib2.AccessFlags

@Suppress("unused")
val anhpanMediaQualityPatch = bytecodePatch(
    name = "Force highest video quality",
    description = "Tries to upload videos to Feed and Story at 1080p with higher bitrate.",
    default = true,
) {
    compatibleWith(
        Compatibility(
            name = "Zalo",
            packageName = "com.zing.zalo",
            apkFileType = ApkFileType.APKM,
            appIconColor = 0x0068FF,
            targets = listOf(AppTarget(version = "26.11.01")),
        ),
    )

    execute {
        VideoCompressResolutionFingerprint.matchAll(0..1).singleOrNull()?.method?.addInstructions(
            0,
            """
                const/high16 v0, 0x44870000
                return v0
            """.trimIndent(),
        )

        VideoCompressParseFingerprint.matchAll(0..1).singleOrNull()?.method?.addInstructions(
            0,
            """
                const-string v0, "1080p"
                iput-object v0, p0, Lcom/zing/zalo/config/VideoNativeCompressConfig;->a:Ljava/lang/String;
                const-string v0, "8000000"
                iput-object v0, p0, Lcom/zing/zalo/config/VideoNativeCompressConfig;->b:Ljava/lang/String;
                iput-object v0, p0, Lcom/zing/zalo/config/VideoNativeCompressConfig;->c:Ljava/lang/String;
                const-string v0, "60"
                iput-object v0, p0, Lcom/zing/zalo/config/VideoNativeCompressConfig;->d:Ljava/lang/String;
                return-void
            """.trimIndent(),
        )
    }
}

private object VideoCompressResolutionFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/config/VideoNativeCompressConfig;",
    name = "c",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "F",
    parameters = emptyList(),
    filters = listOf(string("1080p")),
)

private object VideoCompressParseFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/config/VideoNativeCompressConfig;",
    name = "f",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Lorg/json/JSONObject;"),
    filters = listOf(string("resolution")),
)
