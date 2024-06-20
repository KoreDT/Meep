/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.logger;

import net.kore.meep.api.Meep;

public class Logger {
    private Object grabLogger() {
        Object l = Meep.logger;
        if (l == null) throw new RuntimeException(new IllegalAccessException("No Logger provided"));
        return l;
    }

    private String modifyItem(String origin) {
        return "["+name+"] "+origin;
    }

    private final String name;

    public Logger(String name) {
        this.name = name;
    }

    public void info(String info) {
        info = modifyItem(info);
        try {
            grabLogger().getClass().getMethod("info", String.class).invoke(grabLogger(), info);
        } catch (Throwable ee) {
            try {
                Object c = Class.forName("net.kyori.adventure.text.Component").getMethod("text", String.class).invoke(null, info);
                grabLogger().getClass().getMethod("info", Class.forName("net.kyori.adventure.text.Component")).invoke(grabLogger(), c);
            } catch (Throwable e) {
                throw new IllegalStateException("Logger couldn't be executed.", e);
            }
        }
    }

    public void warn(String info) {
        info = modifyItem(info);
        try {
            grabLogger().getClass().getMethod("warning", String.class).invoke(grabLogger(), info);
        } catch (Throwable eee) {
            try {
                grabLogger().getClass().getMethod("warn", String.class).invoke(grabLogger(), info);
            } catch (Throwable ee) {
                try {
                    Object c = Class.forName("net.kyori.adventure.text.Component").getMethod("text", String.class).invoke(null, info);
                    grabLogger().getClass().getMethod("warn", Class.forName("net.kyori.adventure.text.Component")).invoke(grabLogger(), c);
                } catch (Throwable e) {
                    throw new IllegalStateException("Logger couldn't be executed.", e);
                }
            }
        }
    }

    public void error(String info) {
        info = modifyItem(info);
        try {
            grabLogger().getClass().getMethod("severe", String.class).invoke(grabLogger(), info);
        } catch (Throwable eee) {
            try {
                grabLogger().getClass().getMethod("error", String.class).invoke(grabLogger(), info);
            } catch (Throwable ee) {
                try {
                    Object c = Class.forName("net.kyori.adventure.text.Component").getMethod("text", String.class).invoke(null, info);
                    grabLogger().getClass().getMethod("error", Class.forName("net.kyori.adventure.text.Component")).invoke(grabLogger(), c);
                } catch (Throwable e) {
                    throw new IllegalStateException("Logger couldn't be executed.", e);
                }
            }
        }
    }
}
