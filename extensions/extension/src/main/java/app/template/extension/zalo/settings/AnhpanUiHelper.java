package app.template.extension.zalo.settings;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class AnhpanUiHelper {

    private AnhpanUiHelper() { }

    public static CharSequence filterReadOnlyLabel(CharSequence text) {
        if (text != null && "This chat is locked.".contentEquals(text)) {
            return "(Read Only)";
        }
        return text;
    }

    public static void hideReadOnlySubtitleIfNeeded(View actionBar) {
        if (actionBar == null || !AnhpanPrefs.isReadOnlySubtitleHidden(actionBar.getContext())) {
            return;
        }
        int titleId = actionBar.getResources().getIdentifier(
            "actionbar_txtTitle", "id", actionBar.getContext().getPackageName()
        );
        if (titleId == 0) return;
        View titleView = actionBar.findViewById(titleId);
        if (titleView == null || !(titleView.getParent() instanceof ViewGroup)) return;

        ViewGroup parent = (ViewGroup) titleView.getParent();
        View root = actionBar.getRootView();
        boolean isReadOnly = hasReadOnlyBanner(root);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child != titleView && child instanceof TextView) {
                child.setVisibility(isReadOnly ? View.GONE : View.VISIBLE);
            }
        }
    }

    private static boolean hasReadOnlyBanner(View root) {
        if (root == null) return false;
        if (root instanceof TextView) {
            CharSequence t = ((TextView) root).getText();
            if (t != null && ("This chat is locked.".contentEquals(t) || "(Read Only)".contentEquals(t))) {
                return true;
            }
        }
        if (root instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) root;
            for (int i = 0; i < vg.getChildCount(); i++) {
                if (hasReadOnlyBanner(vg.getChildAt(i))) return true;
            }
        }
        return false;
    }

    public static void removeChatBubbleBorderIfNeeded(View chatRow) {
        if (chatRow == null || !AnhpanPrefs.isChatBubbleBorderRemoved(chatRow.getContext())) {
            return;
        }
        chatRow.setBackground(null);
        if (chatRow instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) chatRow;
            for (int i = 0; i < vg.getChildCount(); i++) {
                View child = vg.getChildAt(i);
                if (child.getBackground() != null) {
                    child.setBackground(null);
                }
            }
        }
    }
}
