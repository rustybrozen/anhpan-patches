package app.template.patches.universal

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

private const val UI_INSPECTOR_CLASS =
    "Lapp/template/extension/universal/inspector/UiInspector;"

private val APPLICATION_SUPERCLASSES = setOf(
    "Landroid/app/Application;",
    "Landroidx/multidex/MultiDexApplication;",
    "Landroid/support/multidex/MultiDexApplication;",
)

internal object ApplicationOnCreateFingerprint : Fingerprint(
    name = "onCreate",
    returnType = "V",
    parameters = emptyList(),
    custom = { _, classDef ->
        classDef.superclass in APPLICATION_SUPERCLASSES
    },
)

@Suppress("unused")
val universalUiInspectorPatch = bytecodePatch(
    name = "Universal UI inspector",
    description = "Adds a draggable debug button to inspect, tag, and export UI element metadata on any app.",
    default = false,
) {
    extendWith("extensions/extension.mpe")

    execute {
        val matches = ApplicationOnCreateFingerprint.matchAll(1..Int.MAX_VALUE)
        for (match in matches) {
            val onCreate = match.method
            val impl = onCreate.implementation ?: continue
            val p0 = impl.registerCount - 1
            onCreate.addInstructions(
                0,
                """
                    move-object/from16 v0, v$p0
                    invoke-static {v0}, $UI_INSPECTOR_CLASS->init(Landroid/app/Application;)V
                """.trimIndent(),
            )
        }
    }
}
