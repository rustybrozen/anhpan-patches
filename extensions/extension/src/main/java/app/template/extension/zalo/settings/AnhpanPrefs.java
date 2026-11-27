package app.template.extension.zalo.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class AnhpanPrefs {
    private static final String PREFS_NAME = "anhpan_zalo_prefs";
    public static final String DISABLE_UNREAD_HIGHLIGHT = "disable_unread_highlight";

    private AnhpanPrefs() { }

    public static SharedPreferences of(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isUnreadHighlightDisabled(Context context) {
        return context != null && of(context).getBoolean(DISABLE_UNREAD_HIGHLIGHT, false);
    }
}
