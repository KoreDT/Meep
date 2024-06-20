/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

import net.kore.meep.api.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface Hostile extends Agressible {
    /**
     * Get the target of the current entity
     * @return {@link Entity}
     */
    @Override
    @NotNull Entity getTarget();
}
