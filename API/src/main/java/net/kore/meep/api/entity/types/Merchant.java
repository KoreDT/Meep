/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

import net.kore.meep.api.item.Item;
import net.kore.meep.api.utils.Pair;

public interface Merchant {
    /**
     * Get the trades of this merchant
     * @return {@link Pair}<{@link Pair}<{@link Item}, {@link Item}>, {@link Item}>
     */
    Pair<Pair<Item, Item>, Item> getTrades();
}
