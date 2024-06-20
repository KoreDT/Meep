/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PersistantStorer {
    @Nullable String getPersistantString(@NotNull String path);
    void setPersistantString(@NotNull String path, @Nullable String value);

    @Nullable Integer getPersistantInteger(@NotNull String path);
    void setPersistantInteger(@NotNull String path, @Nullable Integer value);

    @Nullable Boolean getPersistantBoolean(@NotNull String path);
    void setPersistantBoolean(@NotNull String path, @Nullable Boolean value);

    default @Nullable JsonObject getPersistantJson(@NotNull String path) {
        String s = getPersistantString(path);
        if (s == null) return null;
        return new JsonParser().parse(s).getAsJsonObject();
    }

    default void setPersistantJson(@NotNull String path, @Nullable JsonObject value) {
        if (value == null) {
            setPersistantString(path, null);
            return;
        }
        setPersistantString(path, new Gson().toJson(value));
    }
}
