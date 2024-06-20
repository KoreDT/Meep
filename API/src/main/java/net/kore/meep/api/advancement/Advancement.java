/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.advancement;

import net.kore.meep.api.Keyable;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface Advancement extends Keyable {
    /**
     * Get the display name of the curretn advancement
     * @return Nullable {@link Component}
     */
    @NotNull
    Component getDisplayName();

    /**
     * Get the parent of the current advancement
     * @return Nullable {@link Advancement}
     */
    @Nullable Advancement getParent();

    /**
     * Get a list of all children advancements, the list may be empty.
     * @return Unmodifiable {@link List}<{@link Advancement}>
     */
    @NotNull @Unmodifiable List<Advancement> getChildren();

    /**
     * Get the highest advancement in the chain of this advancement, may return itself.
     * @return {@link Advancement}
     */
    @NotNull Advancement getRoot();
}
