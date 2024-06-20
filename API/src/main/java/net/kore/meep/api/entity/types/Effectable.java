/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

import net.kore.meep.api.potion.PotionEffect;

public interface Effectable {
    /**
     * Give the entity a potion effect
     * @param effect The effect to give the entity
     */
    void effect(PotionEffect effect);

    /**
     * Clear all effects from the entity
     */
    void clearEffects();

    /**
     * Clear a single effect from the entity
     * @param effect The effect to clear
     */
    void clearEffect(PotionEffect effect);
}
