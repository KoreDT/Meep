/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

public interface Ageable {
    /**
     * Get if the entity is an adult
     * @return boolean
     */
    boolean isAdult();

    /**
     * Get if the entity is a child
     * @return boolean
     */
    boolean isChild();

    /**
     * Get the age of the entity
     * @return int
     */
    int getAge();
}
