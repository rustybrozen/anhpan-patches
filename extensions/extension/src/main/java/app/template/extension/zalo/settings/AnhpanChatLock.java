package app.template.extension.zalo.settings;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class AnhpanChatLock {
    public static final int MENU_ITEM_ID = 0x414E4850;
    private static String pendingConvId;

    private AnhpanChatLock() { }

    public static void setPendingConvId(String convId) {
        pendingConvId = convId;
    }

    public static String getMenuTitle(Context context, String convId) {
        return AnhpanPrefs.isChatLocked(context, convId) ? "Enable chat" : "Disable chat";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addLockMenuItem(java.util.List list, Object menuView, Object pupVar) {
        if (list == null || menuView == null || pupVar == null) return;
        try {
            Context context = (Context) menuView.getClass().getMethod("O5").invoke(menuView);
            String convId = (String) pupVar.getClass().getMethod("e").invoke(pupVar);
            pendingConvId = convId;
            if (context == null || convId == null) return;
            Class<?> itemCls = Class.forName("l7j");
            Class<?> typeCls = Class.forName("o7j");
            Object normalType = Enum.valueOf((Class<Enum>) typeCls, "NORMAL");
            java.lang.reflect.Constructor<?> ctor = itemCls.getConstructor(
                int.class,
                android.graphics.drawable.Drawable.class,
                String.class,
                boolean.class,
                typeCls,
                int.class
            );
            Object item = ctor.newInstance(MENU_ITEM_ID, null, getMenuTitle(context, convId), true, normalType, 0);
            list.add(item);
        } catch (Throwable ignored) { }
    }

    public static void wrapListener(Object ej1, Object menuView) {
        if (ej1 == null || menuView == null) return;
        try {
            Context context = (Context) menuView.getClass().getMethod("O5").invoke(menuView);
            if (context == null) return;
            Field hField = ej1.getClass().getField("h");
            Object original = hField.get(ej1);
            if (original == null) return;
            Class<?> listenerType = hField.getType();
            if (!listenerType.isInterface()) return;

            Object proxy = Proxy.newProxyInstance(
                listenerType.getClassLoader(),
                new Class<?>[] { listenerType },
                (p, method, args) -> {
                    if (args != null && args.length > 0 && args[0] != null) {
                        try {
                            Method getId = args[0].getClass().getMethod("getId");
                            Object idVal = getId.invoke(args[0]);
                            if (idVal instanceof Integer && ((Integer) idVal) == MENU_ITEM_ID) {
                                String convId = pendingConvId;
                                boolean locked = AnhpanPrefs.toggleChatLock(context, convId);
                                Toast.makeText(
                                    context,
                                    locked ? "Chat disabled" : "Chat enabled",
                                    Toast.LENGTH_SHORT
                                ).show();
                                try {
                                    Method close = menuView.getClass().getMethod("O6", menuView.getClass().getSuperclass(), boolean.class);
                                    close.invoke(null, menuView, false);
                                } catch (Throwable ignored) {
                                    try {
                                        Method d7 = menuView.getClass().getMethod("d7");
                                        d7.invoke(menuView);
                                    } catch (Throwable ignored2) { }
                                }
                                return null;
                            }
                        } catch (Throwable ignored) { }
                    }
                    return method.invoke(original, args);
                }
            );
            hField.set(ej1, proxy);
        } catch (Throwable ignored) { }
    }

    public static void applyInputBar(View inputBar, String convId) {
        if (inputBar == null || convId == null) return;
        if (AnhpanPrefs.isChatLocked(inputBar.getContext(), convId)) {
            inputBar.setVisibility(View.GONE);
        }
    }

    public static void applyReactionPicker(View picker, String convId) {
        if (picker == null || convId == null) return;
        if (AnhpanPrefs.isChatLocked(picker.getContext(), convId)) {
            picker.setVisibility(View.GONE);
        }
    }
}
