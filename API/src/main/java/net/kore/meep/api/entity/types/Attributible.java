/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.attribute.Attribute;

public interface Attributible {
    /**
     * Get an attribute based on a key
     * @param key The key of the attribute
     * @return {@link Attribute}
     */
    Attribute getAttributeInstance(NamespaceKey key);
}
