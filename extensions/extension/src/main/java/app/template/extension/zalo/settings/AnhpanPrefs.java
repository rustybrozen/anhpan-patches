package app.template.extension.zalo.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class AnhpanPrefs {
    private static final String PREFS_NAME = "anhpan_zalo_prefs";
    public static final String DISABLE_UNREAD_HIGHLIGHT = "disable_unread_highlight";
    public static final String HIDE_QUICK_HEART = "hide_quick_heart";
    public static final String HIDE_QUICK_SHARE = "hide_quick_share";
    private static final String LOCKED_CHATS = "locked_chats";

    private AnhpanPrefs() { }

    public static SharedPreferences of(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isUnreadHighlightDisabled(Context context) {
        return context != null && of(context).getBoolean(DISABLE_UNREAD_HIGHLIGHT, false);
    }

    public static boolean isQuickHeartHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_QUICK_HEART, false);
    }

    public static boolean isQuickShareHidden(Context context) {
        return context != null && of(context).getBoolean(HIDE_QUICK_SHARE, false);
    }

    public static boolean isChatLocked(Context context, String convId) {
        if (context == null || convId == null || convId.isEmpty()) return false;
        java.util.Set<String> locked = of(context).getStringSet(LOCKED_CHATS, null);
        return locked != null && locked.contains(convId);
    }

    public static boolean toggleChatLock(Context context, String convId) {
        if (context == null || convId == null || convId.isEmpty()) return false;
        SharedPreferences prefs = of(context);
        java.util.Set<String> current = prefs.getStringSet(LOCKED_CHATS, null);
        java.util.Set<String> next = current == null ? new java.util.HashSet<>() : new java.util.HashSet<>(current);
        boolean locked;
        if (next.contains(convId)) {
            next.remove(convId);
            locked = false;
        } else {
            next.add(convId);
            locked = true;
        }
        prefs.edit().putStringSet(LOCKED_CHATS, next).apply();
        return locked;
    }
}
