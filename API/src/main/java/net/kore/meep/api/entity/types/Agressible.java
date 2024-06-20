/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

import net.kore.meep.api.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface Agressible {
    /**
     * Get the target of the current entity
     * @return {@link Entity}
     */
    @Nullable Entity getTarget();

    /**
     * Check if the entity has a target
     * @return boolean
     */
    boolean hasTarget();
}
