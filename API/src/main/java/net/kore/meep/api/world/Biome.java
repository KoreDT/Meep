/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.world;

import net.kore.meep.api.Keyable;
import net.kore.meep.api.NamespaceKey;

public class Biome implements Keyable {
    private final NamespaceKey key;

    /**
     * Get the key of the biome
     * @return {@link NamespaceKey}
     */
    @Override
    public NamespaceKey getKey() {
        return key;
    }

    public Biome(NamespaceKey key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Biome biome)) {
            return false;
        }

        return biome.key.getFormatted().equals(key.getFormatted());
    }
}
