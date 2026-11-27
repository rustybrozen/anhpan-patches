package app.template.patches.zalo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.AccessFlags

internal object NormalMsgBindFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/moduleview/message/NormalMsgModuleView;",
    name = "T",
    returnType = "V",
    parameters = listOf("Lpup;", "Z", "I"),
)

@Suppress("unused")
val anhpanChatListFilterPatch = bytecodePatch(
    name = "Chat list filters & custom rename",
    description = "Hide My Cloud, Official Accounts, draft preview, strip emojis, and per-chat preview/avatar/rename.",
    default = true,
) {
    dependsOn(anhpanSettingsPatch)
    compatibleWith(zaloCompatibility)
    extendWith("extensions/extension.mpe")

    execute {
        val match = NormalMsgBindFingerprint.matchOrNull() ?: return@execute
        match.method.addInstructions(
            0,
            "invoke-static {p0, p1}, $ANHPAN_UI_HELPER->onBindNormalMsg(Landroid/view/View;Ljava/lang/Object;)V",
        )
    }
}
