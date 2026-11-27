package app.template.extension.zalo.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class AnhpanPrefs {
    private static final String PREFS_NAME = "anhpan_zalo_prefs";
    public static final String HIDE_SEARCH_BAR = "hide_search_bar";
    public static final String HIDE_QR_BUTTON = "hide_qr_button";
    public static final String HIDE_PLUS_BUTTON = "hide_plus_button";
    public static final String CHAT_HIGHLIGHT_TOGGLE = "chat_highlight_toggle";
    public static final String HIDE_QUICK_HEART = "hide_quick_heart";
    public static final String HIDE_QUICK_SHARE = "hide_quick_share";
    public static final String ENABLE_READ_ONLY = "enable_read_only";
    public static final String READ_ONLY_NO_HIGHLIGHT = "read_only_no_highlight";
    public static final String READ_ONLY_HIDE_AVATAR = "read_only_hide_avatar";
    public static final String READ_ONLY_NO_COLOR_NAME = "read_only_no_color_name";
    public static final String HIDE_READ_ONLY_SUBTITLE = "hide_read_only_subtitle";
    public static final String REMOVE_CHAT_BUBBLE_BORDER = "remove_chat_bubble_border";
    private static final String LOCKED_CHATS = "locked_chats";
    private static final String NO_HIGHLIGHT_CHATS = "no_highlight_chats";

    private AnhpanPrefs() { }

    public static SharedPreferences of(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isSearchBarHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_SEARCH_BAR, false);
    }

    public static boolean isQrButtonHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_QR_BUTTON, false);
    }

    public static boolean isPlusButtonHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_PLUS_BUTTON, false);
    }

    public static boolean isChatHighlightFeatureEnabled(Context context) {
        return context != null && of(context).getBoolean(CHAT_HIGHLIGHT_TOGGLE, true);
    }

    public static boolean isQuickHeartHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_QUICK_HEART, false);
    }

    public static boolean isQuickShareHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_QUICK_SHARE, false);
    }

    public static boolean isReadOnlyEnabled(Context context) {
        return context != null && of(context).getBoolean(ENABLE_READ_ONLY, true);
    }

    public static boolean isReadOnlyNoHighlight(Context context) {
        return context != null && of(context).getBoolean(READ_ONLY_NO_HIGHLIGHT, false);
    }

    public static boolean isReadOnlyHideAvatar(Context context) {
        return context != null && of(context).getBoolean(READ_ONLY_HIDE_AVATAR, false);
    }

    public static boolean isReadOnlyNoColorName(Context context) {
        return context != null && of(context).getBoolean(READ_ONLY_NO_COLOR_NAME, false);
    }

    public static boolean isReadOnlySubtitleHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_READ_ONLY_SUBTITLE, false);
    }

    public static boolean isChatBubbleBorderRemoved(Context context) {
        return context != null && of(context).getBoolean(REMOVE_CHAT_BUBBLE_BORDER, false);
    }

    public static boolean isChatHighlightDisabled(Context context, String convId) {
        return containsInSet(context, NO_HIGHLIGHT_CHATS, convId);
    }

    public static boolean toggleChatHighlightDisabled(Context context, String convId) {
        return toggleInSet(context, NO_HIGHLIGHT_CHATS, convId);
    }

    public static boolean isChatLocked(Context context, String convId) {
        return containsInSet(context, LOCKED_CHATS, convId);
    }

    public static boolean toggleChatLock(Context context, String convId) {
        return toggleInSet(context, LOCKED_CHATS, convId);
    }

    private static boolean containsInSet(Context context, String key, String value) {
        if (context == null || value == null || value.isEmpty()) return false;
        java.util.Set<String> set = of(context).getStringSet(key, null);
        return set != null && set.contains(value);
    }

    private static boolean toggleInSet(Context context, String key, String value) {
        if (context == null || value == null || value.isEmpty()) return false;
        SharedPreferences prefs = of(context);
        java.util.Set<String> current = prefs.getStringSet(key, null);
        java.util.Set<String> next = current == null ? new java.util.HashSet<>() : new java.util.HashSet<>(current);
        boolean added;
        if (next.contains(value)) {
            next.remove(value);
            added = false;
        } else {
            next.add(value);
            added = true;
        }
        prefs.edit().putStringSet(key, next).apply();
        return added;
    }
}
