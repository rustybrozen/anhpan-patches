package app.template.patches.zalo

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.addInstructionsWithLabels
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.fieldAccess
import app.morphe.patcher.methodCall
import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.smali.ExternalLabel
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

private const val ANHPAN_SETTINGS = "Lapp/template/extension/zalo/settings/AnhpanSettings;"
private const val ANHPAN_CHAT_LOCK = "Lapp/template/extension/zalo/settings/AnhpanChatLock;"

internal object ZaloSettingsFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/settings/SettingV2View;",
    returnType = "Landroid/view/View;",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Landroid/view/LayoutInflater;", "Landroid/widget/LinearLayout;", "Landroid/os/Bundle;"),
    filters = listOf(
        fieldAccess(opcode = Opcode.SGET, name = "setting_v2_view", type = "I"),
        methodCall(definingClass = "Landroid/view/LayoutInflater;", name = "inflate"),
        fieldAccess(opcode = Opcode.SGET, name = "itemPrivacy", type = "I"),
    ),
)

internal object TabMsgUnreadHighlightFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/moduleview/message/TabMsgCommonItemModuleView;",
    name = "Z",
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC),
    parameters = listOf("Lpup;", "I"),
)

internal object ContextMenuW6Fingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/contextmenu/ConversationContextMenuView;",
    returnType = "Lcom/zing/zalo/zdesign/component/popover/PopoverView;",
    parameters = listOf("Lpup;"),
)

internal object ChatInputBarInitFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/widget/inputbar/ChatInputBar;",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;", "Ljava/lang/String;", "Lz34;", "Ls64;", "Lnh7;"),
)

internal object ReactionPickerInitFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/widget/reaction/ReactionPickerView;",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;", "Z", "Ljava/lang/String;", "Lytk;", "Ljava/lang/String;"),
)

internal object ChatRowReactionBarFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/chatrow/ChatRow;",
    name = "I",
    returnType = "Z",
    parameters = listOf("Ljug;", "Lvv3;"),
)

internal object ChatRowShareBtnFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/chatrow/ChatRow;",
    name = "R0",
    returnType = "Z",
    parameters = emptyList(),
)

internal object ChatRowAvatarClickFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/chatrow/ChatRow;",
    name = "T1",
    returnType = "Z",
    parameters = listOf("F", "F", "I"),
)

internal object ChatActionBarBtnFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/actionbar/ChatViewActionBarButton;",
    name = "setIconResId",
    returnType = "V",
    parameters = listOf("I"),
)

internal object ChatInfoViewCreateFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/rightmenu/ChatInfoView;",
    name = "n5",
    returnType = "Landroid/view/View;",
    parameters = listOf("Landroid/view/LayoutInflater;", "Landroid/view/ViewGroup;", "Landroid/os/Bundle;"),
)

internal object ChatViewActionBarT6Fingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/ChatView;",
    name = "T6",
    returnType = "Lcom/zing/zalo/ui/chat/actionbar/ChatViewActionBarButton;",
    parameters = listOf("Lim4;", "I"),
)

internal object SettingHorizontalBindFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/moduleview/chatinfo/ChatInfoModuleViews\$SettingHorizontalModuleView;",
    name = "g",
    returnType = "V",
    parameters = listOf("La04;", "I"),
)

internal object ChatSettingBindFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/moduleview/chatinfo/ChatInfoModuleViews\$ChatSettingModuleView;",
    name = "g",
    returnType = "V",
    parameters = listOf("La04;", "I"),
)

internal object ChatRowOnDrawFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/chatrow/ChatRow;",
    name = "onDraw",
    returnType = "V",
    parameters = listOf("Landroid/graphics/Canvas;"),
)

internal object ChatRowSenderNameFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/chat/chatrow/ChatRow;",
    name = "e0",
    returnType = "V",
    parameters = listOf("Landroid/graphics/Canvas;", "Z"),
)

internal object MainTabMenuFingerprint : Fingerprint(
    definingClass = "Lcom/zing/zalo/ui/maintab/MainTabView;",
    name = "m5",
    returnType = "V",
    parameters = listOf("Lcom/zing/zalo/zview/actionbar/ActionBarMenu;"),
)

@Suppress("unused")
val anhpanSettingsPatch = bytecodePatch(
    name = "Anhpan's settings",
    description = "Adds Anhpan's settings entry and message tweaks to Zalo.",
    default = true,
) {
    compatibleWith(Compatibility(
        name = "Zalo",
        packageName = "com.zing.zalo",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x0068FF,
        targets = listOf(
            AppTarget(version = "26.11.01"),
            AppTarget(version = null, isExperimental = true),
        ),
    ))

    extendWith("extensions/extension.mpe")

    execute {
        check(ZaloSettingsFingerprint.matchAll(1..1).size == 1)
        val settings = ZaloSettingsFingerprint.method
        val callTarget = "$ANHPAN_SETTINGS->attach(Landroid/view/View;Landroid/view/View;)V"
        if (settings.implementation!!.instructions.none {
            (it as? ReferenceInstruction)?.reference?.toString() == callTarget
        }) {
            val privacyIndex = settings.implementation!!.instructions.indexOfFirst {
                it.opcode == Opcode.SGET &&
                    ((it as? ReferenceInstruction)?.reference as? FieldReference)?.name == "itemPrivacy"
            }
            check(privacyIndex >= 0)
            val lookup = settings.getInstruction<FiveRegisterInstruction>(privacyIndex + 1)
            check(lookup.opcode == Opcode.INVOKE_STATIC && lookup.registerCount == 2)
            val privacy = settings.getInstruction<OneRegisterInstruction>(privacyIndex + 2)
            check(privacy.opcode == Opcode.MOVE_RESULT_OBJECT)
            check(settings.getInstruction<OneRegisterInstruction>(privacyIndex + 3).opcode == Opcode.CHECK_CAST)
            val branch = settings.getInstruction<OneRegisterInstruction>(privacyIndex + 4)
            check(branch.opcode == Opcode.IF_EQZ && branch.registerA == privacy.registerA)

            settings.addInstructions(
                privacyIndex + 5,
                "invoke-static {v${lookup.registerC}, v${privacy.registerA}}, $callTarget",
            )
        }

        check(TabMsgUnreadHighlightFingerprint.matchAll(1..1).size == 1)
        val unreadMethod = TabMsgUnreadHighlightFingerprint.method
        val firstInstruction = unreadMethod.getInstruction(0)
        unreadMethod.addInstructionsWithLabels(
            0,
            """
                invoke-static {p0, p1}, $ANHPAN_SETTINGS->shouldDisableUnreadHighlight(Landroid/view/View;Ljava/lang/Object;)Z
                move-result v0
                if-eqz v0, :anhpan_original_unread
                const/4 v0, 0x0
                return v0
            """.trimIndent(),
            ExternalLabel("anhpan_original_unread", firstInstruction),
        )

        check(ContextMenuW6Fingerprint.matchAll(1..1).size == 1)
        val w6Method = ContextMenuW6Fingerprint.method
        val y6CallIndex = w6Method.implementation!!.instructions.indexOfFirst {
            (it.opcode == Opcode.INVOKE_VIRTUAL || it.opcode == Opcode.INVOKE_SUPER) &&
                ((it as? ReferenceInstruction)?.reference as? MethodReference)?.name == "Y6"
        }
        check(y6CallIndex >= 0)
        val listReg = w6Method.getInstruction<OneRegisterInstruction>(y6CallIndex + 1).registerA
        val n1SetIndex = w6Method.implementation!!.instructions.indexOfFirst {
            it.opcode == Opcode.IPUT_OBJECT &&
                ((it as? ReferenceInstruction)?.reference as? FieldReference)?.name == "N1"
        }
        check(n1SetIndex >= 0)
        val ej1Reg = w6Method.getInstruction<OneRegisterInstruction>(n1SetIndex).registerA

        w6Method.addInstructions(
            n1SetIndex + 1,
            "invoke-static {v$ej1Reg, p0}, $ANHPAN_CHAT_LOCK->wrapListener(Ljava/lang/Object;Ljava/lang/Object;)V",
        )
        w6Method.addInstructions(
            y6CallIndex + 2,
            "invoke-static {v$listReg, p0}, $ANHPAN_CHAT_LOCK->addLockMenuItem(Ljava/util/List;Ljava/lang/Object;)V",
        )
        w6Method.addInstructions(
            0,
            "invoke-static {p1}, $ANHPAN_CHAT_LOCK->captureConvId(Ljava/lang/Object;)V",
        )

        check(ChatInputBarInitFingerprint.matchAll(1..1).size == 1)
        val inputBarInit = ChatInputBarInitFingerprint.method
        val inputBarReturn = inputBarInit.implementation!!.instructions.indexOfLast { it.opcode == Opcode.RETURN_VOID }
        check(inputBarReturn >= 0)
        val inputBarBase = inputBarInit.implementation!!.registerCount - 6
        inputBarInit.addInstructions(
            inputBarReturn,
            """
                move-object/from16 v0, v$inputBarBase
                move-object/from16 v1, v${inputBarBase + 2}
                invoke-static {v0, v1}, $ANHPAN_CHAT_LOCK->applyInputBar(Landroid/view/View;Ljava/lang/String;)V
            """.trimIndent(),
        )

        check(ReactionPickerInitFingerprint.matchAll(1..1).size == 1)
        val reactionInit = ReactionPickerInitFingerprint.method
        val reactionReturn = reactionInit.implementation!!.instructions.indexOfLast { it.opcode == Opcode.RETURN_VOID }
        check(reactionReturn >= 0)
        val reactionBase = reactionInit.implementation!!.registerCount - 6
        reactionInit.addInstructions(
            reactionReturn,
            """
                move-object/from16 v0, v$reactionBase
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->applyReactionPicker(Landroid/view/View;)V
            """.trimIndent(),
        )

        check(ChatRowReactionBarFingerprint.matchAll(1..1).size == 1)
        val rowReaction = ChatRowReactionBarFingerprint.method
        val rowReactionFirst = rowReaction.getInstruction(0)
        rowReaction.addInstructionsWithLabels(
            0,
            """
                invoke-static {p0}, $ANHPAN_CHAT_LOCK->shouldHideQuickHeart(Landroid/view/View;)Z
                move-result v0
                if-eqz v0, :anhpan_orig_heart
                const/4 v0, 0x0
                return v0
            """.trimIndent(),
            ExternalLabel("anhpan_orig_heart", rowReactionFirst),
        )

        check(ChatRowShareBtnFingerprint.matchAll(1..1).size == 1)
        val rowShare = ChatRowShareBtnFingerprint.method
        val rowShareFirst = rowShare.getInstruction(0)
        rowShare.addInstructionsWithLabels(
            0,
            """
                invoke-static {p0}, $ANHPAN_CHAT_LOCK->shouldHideQuickShare(Landroid/view/View;)Z
                move-result v0
                if-eqz v0, :anhpan_orig_share
                const/4 v0, 0x0
                return v0
            """.trimIndent(),
            ExternalLabel("anhpan_orig_share", rowShareFirst),
        )

        check(ChatRowAvatarClickFingerprint.matchAll(1..1).size == 1)
        val rowAvatar = ChatRowAvatarClickFingerprint.method
        val rowAvatarFirst = rowAvatar.getInstruction(0)
        rowAvatar.addInstructionsWithLabels(
            0,
            """
                invoke-static {}, $ANHPAN_CHAT_LOCK->isActiveConvLocked()Z
                move-result v0
                if-eqz v0, :anhpan_orig_avatar
                const/4 v0, 0x0
                return v0
            """.trimIndent(),
            ExternalLabel("anhpan_orig_avatar", rowAvatarFirst),
        )

        check(ChatActionBarBtnFingerprint.matchAll(1..1).size == 1)
        val actionBtn = ChatActionBarBtnFingerprint.method
        actionBtn.addInstructions(
            0,
            "invoke-static {p0, p1}, $ANHPAN_CHAT_LOCK->applyActionBarButton(Landroid/view/View;I)V",
        )

        check(ChatInfoViewCreateFingerprint.matchAll(1..1).size == 1)
        val infoCreate = ChatInfoViewCreateFingerprint.method
        val infoRet = infoCreate.implementation!!.instructions.indexOfLast { it.opcode == Opcode.RETURN_OBJECT }
        check(infoRet >= 0)
        val infoRetReg = infoCreate.getInstruction<OneRegisterInstruction>(infoRet).registerA
        infoCreate.addInstructions(
            infoRet,
            "invoke-static {v$infoRetReg}, $ANHPAN_CHAT_LOCK->applyChatInfoView(Landroid/view/View;)V",
        )

        check(ChatViewActionBarT6Fingerprint.matchAll(1..1).size == 1)
        val t6Method = ChatViewActionBarT6Fingerprint.method
        val t6Ret = t6Method.implementation!!.instructions.indexOfLast { it.opcode == Opcode.RETURN_OBJECT }
        check(t6Ret >= 0)
        val t6RetReg = t6Method.getInstruction<OneRegisterInstruction>(t6Ret).registerA
        val t6Base = t6Method.implementation!!.registerCount - 3
        t6Method.addInstructions(
            t6Ret,
            """
                move-object/from16 v0, v$t6RetReg
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->applyCapturedActionBarButton(Landroid/view/View;)V
            """.trimIndent(),
        )
        t6Method.addInstructions(
            0,
            """
                move/from16 v0, v${t6Base + 2}
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->captureActionResId(I)V
            """.trimIndent(),
        )

        check(SettingHorizontalBindFingerprint.matchAll(1..1).size == 1)
        val hBind = SettingHorizontalBindFingerprint.method
        val hBindBase = hBind.implementation!!.registerCount - 3
        hBind.addInstructions(
            0,
            """
                move-object/from16 v0, v${hBindBase + 1}
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->filterHorizontalItems(Ljava/lang/Object;)V
            """.trimIndent(),
        )

        check(ChatSettingBindFingerprint.matchAll(1..1).size == 1)
        val sBind = ChatSettingBindFingerprint.method
        val sBindBase = sBind.implementation!!.registerCount - 3
        val sBindRet = sBind.implementation!!.instructions.indexOfLast { it.opcode == Opcode.RETURN_VOID }
        check(sBindRet >= 0)
        sBind.addInstructions(
            sBindRet,
            """
                move-object/from16 v0, v$sBindBase
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->applySettingHideTag(Landroid/view/View;)V
            """.trimIndent(),
        )
        sBind.addInstructions(
            0,
            """
                move-object/from16 v0, v$sBindBase
                move-object/from16 v1, v${sBindBase + 1}
                invoke-static {v0, v1}, $ANHPAN_CHAT_LOCK->tagSettingItem(Landroid/view/View;Ljava/lang/Object;)V
            """.trimIndent(),
        )

        check(ChatRowOnDrawFingerprint.matchAll(1..1).size == 1)
        val rowDraw = ChatRowOnDrawFingerprint.method
        val rowDrawBase = rowDraw.implementation!!.registerCount - 2
        rowDraw.addInstructions(
            0,
            """
                move-object/from16 v0, v$rowDrawBase
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->applyChatRowDraw(Landroid/view/View;)V
            """.trimIndent(),
        )

        check(ChatRowSenderNameFingerprint.matchAll(1..1).size == 1)
        val rowName = ChatRowSenderNameFingerprint.method
        val rowNameBase = rowName.implementation!!.registerCount - 3
        rowName.addInstructions(
            0,
            """
                move-object/from16 v0, v$rowNameBase
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->applySenderNameColor(Landroid/view/View;)V
            """.trimIndent(),
        )

        check(MainTabMenuFingerprint.matchAll(1..1).size == 1)
        val tabMenu = MainTabMenuFingerprint.method
        val tabMenuBase = tabMenu.implementation!!.registerCount - 2
        val tabMenuRet = tabMenu.implementation!!.instructions.indexOfLast { it.opcode == Opcode.RETURN_VOID }
        check(tabMenuRet >= 0)
        tabMenu.addInstructions(
            tabMenuRet,
            """
                move-object/from16 v0, v$tabMenuBase
                invoke-static {v0}, $ANHPAN_CHAT_LOCK->applyMainTabMenu(Ljava/lang/Object;)V
            """.trimIndent(),
        )
    }
}
