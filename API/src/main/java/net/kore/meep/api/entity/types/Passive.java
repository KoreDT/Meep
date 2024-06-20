/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

public interface Passive {
    /**
     * Check if the entity will target other mobs, this does not include the player
     * @return boolean
     */
    boolean targetsOtherMobs();

    /**
     * Check if the mob can grant the player damage, but via other methods of damage (e.g. pufferfish)
     * @return boolean
     */
    boolean grantsPassivePlayerDamage();
}
