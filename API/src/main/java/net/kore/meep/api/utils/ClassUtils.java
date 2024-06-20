/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.utils;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ClassUtils {
    public static String callerClassName() {
        return callerClassName(2);
    }

    public static String callerClassName(int skip) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length <= skip) {
            throw new IllegalArgumentException("Not enough stack elements to skip " + skip + " elements");
        } else {
            return elements[skip + 2].getClassName();
        }
    }

    public static @Nullable Class<?> getClassOrNull(@Nullable String name) {
        if (name == null) return null;
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static @Nullable Method getMethodOrNull(@Nullable Class<?> clazz, @Nullable String name, Class<?>... args) {
        if (clazz == null || name == null) return null;
        try {
            return clazz.getMethod(name, args);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static @Nullable Field getFieldOrNull(@Nullable Class<?> clazz, @Nullable String name) {
        if (clazz == null || name == null) return null;
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
