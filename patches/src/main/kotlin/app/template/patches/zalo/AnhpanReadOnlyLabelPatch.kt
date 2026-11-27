package app.template.patches.zalo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import com.android.tools.smali.dexlib2.AccessFlags

internal const val ANHPAN_UI_HELPER = "Lapp/template/extension/zalo/settings/AnhpanUiHelper;"

internal object ChatInputBarSetTextFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/widget/RobotoTextView;",
    name = "setText",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC),
    parameters = listOf("Ljava/lang/CharSequence;", "Landroid/widget/TextView\$BufferType;"),
)

@Suppress("unused")
val anhpanReadOnlyLabelPatch = bytecodePatch(
    name = "Rename read-only label",
    description = "Replaces 'This chat is locked.' with '(Read Only)' in read-only chats.",
    default = true,
) {
    dependsOn(anhpanSettingsPatch)
    compatibleWith(zaloCompatibility)
    extendWith("extensions/extension.mpe")

    execute {
        val match = ChatInputBarSetTextFingerprint.matchOrNull() ?: return@execute
        match.method.addInstructions(
            0,
            """
                invoke-static {p1}, $ANHPAN_UI_HELPER->filterReadOnlyLabel(Ljava/lang/CharSequence;)Ljava/lang/CharSequence;
                move-result-object p1
            """.trimIndent(),
        )
    }
}
