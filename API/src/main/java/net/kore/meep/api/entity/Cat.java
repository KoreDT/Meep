/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.color.DyeColor;
import net.kore.meep.api.entity.types.Breedable;
import net.kore.meep.api.entity.types.Tameable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public interface Cat extends Entity, Breedable, Tameable {
    Type getType();
    void setType(Type type);
    DyeColor getCollarColor();
    void setCollarColor(DyeColor dyeColor);

    boolean isLayingDown();
    void setLayingDown(boolean layingDown);

    enum Type {
        TABBY("tabby"),
        BLACK("black"),
        RED("red"),
        SIAMESE("siamese"),
        BRITISH_SHORTHAIR("british_shorthair"),
        CALICO("calico"),
        PERSIAN("persian"),
        RAGDOLL("ragdoll"),
        WHITE("white"),
        JELLIE("jellie"),
        ALL_BLACK("all_black");

        private final NamespaceKey key;

        Type(String key) {
            this.key = NamespaceKey.MINECRAFT(key, NamespaceKey.KeyType.OTHER);
        }

        public @NotNull NamespaceKey getKey() {
            return this.key;
        }
    }
}
