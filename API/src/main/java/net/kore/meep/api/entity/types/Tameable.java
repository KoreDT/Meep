/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

import org.jetbrains.annotations.Nullable;

public interface Tameable {
    /**
     * Get the owner of the current tameable entity, may be null
     * @return {@link Tamer}
     */
    @Nullable Tamer getOwner();
}
