/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

public interface Breedable {
    /**
     * Get if the current entity can breed with another
     * @param other The other breedable entity
     * @return boolean
     */
    boolean canBreedWith(Breedable other);
}
