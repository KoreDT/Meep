/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.item;

import net.kore.meep.api.Keyable;

public interface Item extends Keyable {
    /**
     * Get the item meta of this item
     * @return {@link ItemMeta}
     */
    ItemMeta getItemMeta();

    /**
     * Set the item meta of the item
     * @param itemMeta The item meta
     */
    void setItemMeta(ItemMeta itemMeta);

    /**
     * Get the damage on the item
     * @return short
     */
    short getDamage();

    /**
     * Set the damage on the item
     * @param damage The damage
     */
    void setDamage(short damage);

    default boolean equals(Item other) {
        return other.getDamage() == getDamage() && other.getKey().equals(getKey()) && other.getItemMeta().equals(getItemMeta());
    }

    Item clone();
}
