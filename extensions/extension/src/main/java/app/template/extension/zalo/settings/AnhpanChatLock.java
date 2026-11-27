package app.template.extension.zalo.settings;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class AnhpanChatLock {
    private static final String TAG = "AnhpanChatLock";
    public static final int MENU_ITEM_ID = 0x414E4850;
    private static String pendingConvId;

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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void addLockMenuItem(java.util.List list, Object menuView, Object pupVar) {
        if (list == null || menuView == null || pupVar == null) return;
        try {
            Context context = (Context) findMethod(menuView.getClass(), "O5").invoke(menuView);
            String convId = (String) findMethod(pupVar.getClass(), "e").invoke(pupVar);
            pendingConvId = convId;
            if (context == null || convId == null) return;
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
