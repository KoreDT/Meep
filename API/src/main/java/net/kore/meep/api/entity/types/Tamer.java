/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

public interface Tamer {
    /**
     * @return <code>true</code> if the {@link Tameable} was not tamed or <code>false</code> if the {@link Tameable} was tamed previously.
     */
    boolean tame(Tameable tameable);
}
