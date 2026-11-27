package app.template.patches.zalo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.AccessFlags

internal object ZdsActionBarLayoutFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/zdesign/component/ZdsActionBar;",
    name = "onLayout",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PROTECTED),
    parameters = listOf("Z", "I", "I", "I", "I"),
)

@Suppress("unused")
val anhpanHideReadOnlySubtitlePatch = bytecodePatch(
    name = "Hide read-only subtitle",
    description = "Hides the subtitle below the chat title when in read-only mode (configurable in Anhpan's settings).",
    default = true,
) {
    dependsOn(anhpanSettingsPatch)
    compatibleWith(zaloCompatibility)
    extendWith("extensions/extension.mpe")

    execute {
        val match = ZdsActionBarLayoutFingerprint.matchOrNull() ?: return@execute
        match.method.addInstructions(
            0,
            "invoke-static {p0}, $ANHPAN_UI_HELPER->hideReadOnlySubtitleIfNeeded(Landroid/view/View;)V",
        )
    }
}
