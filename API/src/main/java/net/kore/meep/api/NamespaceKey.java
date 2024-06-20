/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api;

import net.kore.meep.api.utils.TriState;
import org.jetbrains.annotations.NotNull;

public class NamespaceKey {
    private final String namespace;
    public String getNamespace() {
        return namespace;
    }

    private final String path;
    public String getPath() {
        return path;
    }

    private KeyType type;
    public KeyType getType() {
        return type;
    }
    public NamespaceKey setType(KeyType type) {
        this.type = type;
        return this;
    }

    public String getFormatted() {
        return namespace + ":" + path;
    }

    public NamespaceKey(String namespace, String path) {
        this(namespace, path, KeyType.OTHER);
    }

    public NamespaceKey(String namespace, String path, KeyType type) {
        this.namespace = namespace;
        this.path = path;
        this.type = type;
    }

    public static NamespaceKey MINECRAFT(String path) {
        return new NamespaceKey("minecraft", path);
    }
    public static @NotNull NamespaceKey MINECRAFT(String path, KeyType type) {
        return new NamespaceKey("minecraft", path, type);
    }

    public static NamespaceKey fromFormatted(String formatted) {
        return fromFormatted(formatted, KeyType.OTHER);
    }

    public static NamespaceKey fromFormatted(String formatted, KeyType type) {
        String[] formatteds = formatted.split(":");
        if (formatteds.length != 2) return null;
        return new NamespaceKey(formatteds[0], formatteds[1], type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof NamespaceKey that) {
            return exactEquals(that).asBoolean();
        }

        return false;
    }

    /**
     * Returns {@link TriState}.FALSE if the namespace or path are not equal<br>
     * <pre><code>this.namespace != that.namespace || this.path != that.path // Type does not matter</code></pre><br><br>
     * Returns {@link TriState}.TRUEISH if the namespace and path are equal but the type is not<br>
     * <pre><code>this.namespace == that.namespace && this.path == that.path && this.type != that.type</code></pre><br><br>
     * Returns {@link TriState}.TRUE if the namespace, path and type are all equal<br>
     * <pre><code>this.namespace == that.namespace && this.path == that.path && this.type == that.type</code></pre>
     */
    public TriState exactEquals(NamespaceKey that) {
        if (this == that) return TriState.TRUE;
        if (this.getNamespace().equals(that.getNamespace()) && this.getPath().equals(that.getPath())) {
            if (this.getType().equals(that.getType())) {
                return TriState.TRUE;
            } else {
                return TriState.TRUEISH;
            }
        }
        return TriState.FALSE;
    }

    public enum KeyType {
        ITEM,
        BLOCK,
        ENTITY,
        ENCHANT,
        WORLD,
        ADVANCEMENT,
        PARTICLE,
        VILLAGER_TYPE,
        VILLAGER_PROFESSION,
        OTHER
    }
}
