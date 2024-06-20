/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.enchant;

import net.kore.meep.api.Keyable;
import net.kore.meep.api.NamespaceKey;

public class Enchant implements Keyable {
    private final NamespaceKey key;

    /**
     * Get the key of this enchant
     * @return {@link NamespaceKey}
     */
    @Override
    public NamespaceKey getKey() {
        return key;
    }

    /**
     * Create a new enchant reference
     * @param key The key of the enchant
     */
    public Enchant(NamespaceKey key) {
        this.key = key;
    }
}