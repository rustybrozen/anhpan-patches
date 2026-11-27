package app.template.patches.zalo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.AccessFlags

internal object ChatRowTextLayoutFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/chatrow/ChatRowText;",
    name = "onLayout",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PROTECTED),
    parameters = listOf("Z", "I", "I", "I", "I"),
)

@Suppress("unused")
val anhpanRemoveChatBubbleBorderPatch = bytecodePatch(
    name = "Remove chat bubble border",
    description = "Removes the chat bubble border/background on text messages (configurable in Anhpan's settings).",
    default = true,
) {
    dependsOn(anhpanSettingsPatch)
    compatibleWith(zaloCompatibility)
    extendWith("extensions/extension.mpe")

    execute {
        val match = ChatRowTextLayoutFingerprint.matchOrNull() ?: return@execute
        match.method.addInstructions(
            0,
            "invoke-static {p0}, $ANHPAN_UI_HELPER->removeChatBubbleBorderIfNeeded(Landroid/view/View;)V",
        )
    }
}
