package app.template.extension.zalo.settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.template.extension.zalo.ui.ZaloUi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class AnhpanChatLock {
    private static final String TAG = "AnhpanChatLock";
    public static final int MENU_ITEM_ID = 0x414E4850;
    private static String pendingConvId;
    private static boolean activeConvLocked;

    private AnhpanChatLock() { }

    public static void setPendingConvId(String convId) {
        pendingConvId = convId;
    }

    public static String getMenuTitle(Context context, String convId) {
        return AnhpanPrefs.isChatLocked(context, convId) ? "Enable chat" : "Disable chat";
    }

    private static Method findMethod(Class<?> cls, String name, Class<?>... params) throws NoSuchMethodException {
        for (Class<?> cur = cls; cur != null; cur = cur.getSuperclass()) {
            try {
                Method m = cur.getDeclaredMethod(name, params);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException ignored) { }
        }
        throw new NoSuchMethodException(cls.getName() + "." + name);
    }

    private static Field findField(Class<?> cls, String name) throws NoSuchFieldException {
        for (Class<?> cur = cls; cur != null; cur = cur.getSuperclass()) {
            try {
                Field f = cur.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException ignored) { }
        }
        throw new NoSuchFieldException(cls.getName() + "." + name);
    }

    public static void captureConvId(Object pupVar) {
        if (pupVar == null) return;
        try {
            pendingConvId = (String) findMethod(pupVar.getClass(), "e").invoke(pupVar);
        } catch (Throwable t) {
            Log.e(TAG, "captureConvId failed", t);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addLockMenuItem(java.util.List list, Object menuView) {
        String convId = pendingConvId;
        if (list == null || menuView == null || convId == null) return;
        try {
            Context context = (Context) findMethod(menuView.getClass(), "O5").invoke(menuView);
            if (context == null) return;
            ClassLoader cl = menuView.getClass().getClassLoader();
            Class<?> itemCls = cl.loadClass("l7j");
            Class<?> typeCls = cl.loadClass("o7j");
            Object normalType = Enum.valueOf((Class<Enum>) typeCls, "NORMAL");
            Constructor<?> ctor = itemCls.getDeclaredConstructor(
                int.class,
                android.graphics.drawable.Drawable.class,
                String.class,
                boolean.class,
                typeCls,
                int.class
            );
            ctor.setAccessible(true);
            Object item = ctor.newInstance(MENU_ITEM_ID, null, getMenuTitle(context, convId), true, normalType, 0);
            list.add(item);
        } catch (Throwable t) {
            Log.e(TAG, "addLockMenuItem failed", t);
        }
    }

    public static void wrapListener(Object ej1, Object menuView) {
        if (ej1 == null || menuView == null) return;
        try {
            Context context = (Context) findMethod(menuView.getClass(), "O5").invoke(menuView);
            if (context == null) return;
            Field hField = findField(ej1.getClass(), "h");
            Object original = hField.get(ej1);
            if (original == null) return;
            Class<?>[] interfaces = original.getClass().getInterfaces();
            if (interfaces.length == 0) {
                Log.e(TAG, "original listener has no interfaces: " + original.getClass().getName());
                return;
            }

            Object proxy = Proxy.newProxyInstance(
                original.getClass().getClassLoader(),
                interfaces,
                (p, method, args) -> {
                    if (args != null) {
                        for (Object arg : args) {
                            if (arg == null) continue;
                            try {
                                Object idVal = null;
                                if ("l7j".equals(arg.getClass().getName())) {
                                    idVal = findMethod(arg.getClass(), "getId").invoke(arg);
                                } else if (arg instanceof Integer) {
                                    idVal = arg;
                                }
                                if (idVal instanceof Integer && ((Integer) idVal) == MENU_ITEM_ID) {
                                    String convId = pendingConvId;
                                    boolean locked = AnhpanPrefs.toggleChatLock(context, convId);
                                    Toast.makeText(
                                        context,
                                        locked ? "Chat disabled" : "Chat enabled",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                    try {
                                        findMethod(menuView.getClass(), "d7").invoke(menuView);
                                    } catch (Throwable ignored) { }
                                    return null;
                                }
                            } catch (Throwable t) {
                                Log.e(TAG, "proxy click error", t);
                            }
                        }
                    }
                    return method.invoke(original, args);
                }
            );
            hField.set(ej1, proxy);
        } catch (Throwable t) {
            Log.e(TAG, "wrapListener failed", t);
        }
    }

    public static void applyInputBar(View inputBar, String convId) {
        if (inputBar == null || convId == null) {
            activeConvLocked = false;
            return;
        }
        Context context = inputBar.getContext();
        activeConvLocked = AnhpanPrefs.isChatLocked(context, convId);
        if (!activeConvLocked) return;
        if (!(inputBar instanceof ViewGroup)) {
            inputBar.setVisibility(View.GONE);
            return;
        }
        ViewGroup group = (ViewGroup) inputBar;
        for (int i = 0; i < group.getChildCount(); i++) {
            group.getChildAt(i).setVisibility(View.GONE);
        }
        boolean dark = ZaloUi.isDarkMode(context, inputBar);
        int bg = dark ? 0xFF1F2229 : 0xFFF4F5F7;
        int border = dark ? 0xFF2D313B : 0xFFE5E7EB;
        int textColor = dark ? 0xFFB0B5C0 : 0xFF5A606D;
        int iconColor = 0xFF4D8EFF;

        LinearLayout banner = new LinearLayout(context);
        banner.setTag("anhpan-locked-banner");
        banner.setOrientation(LinearLayout.HORIZONTAL);
        banner.setGravity(Gravity.CENTER);
        banner.setBackgroundColor(bg);
        banner.setClickable(true);
        banner.setFocusable(true);
        int padV = dp(context, 14);
        int padH = dp(context, 16);
        banner.setPadding(padH, padV, padH, padV);

        View divider = new View(context);
        divider.setBackgroundColor(border);

        TextView icon = new TextView(context);
        icon.setText("i");
        icon.setTextColor(iconColor);
        icon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        icon.setTypeface(Typeface.SERIF, Typeface.BOLD);
        icon.setGravity(Gravity.CENTER);
        GradientDrawable circle = new GradientDrawable();
        circle.setShape(GradientDrawable.OVAL);
        circle.setStroke(dp(context, 1.5f), iconColor);
        icon.setBackground(circle);
        int iconSize = dp(context, 18);
        LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(iconSize, iconSize);
        iconLp.setMarginEnd(dp(context, 8));
        banner.addView(icon, iconLp);

        TextView label = new TextView(context);
        label.setText("This chat is locked.");
        label.setTextColor(textColor);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        banner.addView(label);

        LinearLayout wrapper = new LinearLayout(context);
        wrapper.setOrientation(LinearLayout.VERTICAL);
        wrapper.addView(divider, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(context, 1)));
        wrapper.addView(banner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        group.addView(wrapper, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private static int dp(Context context, float value) {
        return Math.round(TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            context.getResources().getDisplayMetrics()
        ));
    }

    public static void applyReactionPicker(View picker) {
        if (picker != null && activeConvLocked) {
            picker.setVisibility(View.GONE);
        }
    }
}
