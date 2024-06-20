/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.UUID;

public interface Attribute {
    /**
     * Get the current base value
     * @return double
     */
    double getBaseValue();

    /**
     * Sets the base value
     * @param value The value to set base at
     */
    void setBaseValue(double value);

    /**
     * Get all modifiers for this attribute
     * @return Unmodifiable {@link List}<{@link AttributeMod}>
     */
    @NotNull @Unmodifiable List<AttributeMod> getModifiers();

    /**
     * Get an attribute modifier based of off it's UUID
     * @param uuid The UUID of the modifier
     * @return {@link AttributeMod}
     */
    @Nullable AttributeMod getModifier(@NotNull UUID uuid);

    /**
     * Add a modifier to the current attribute
     * @param mod The modifier to add
     */
    void addModifier(@NotNull AttributeMod mod);

    /**
     * Remove a modifier from the current attribute
     * @param mod The modifier to remove
     */
    void removeModifier(@Nullable AttributeMod mod);

    /**
     * Remove a modifier from the current attribute by it's UUID
     * @param uuid The modifier's UUID to remove
     */
    void removeModifier(@NotNull UUID uuid);

    /**
     * Get the value of the attribute with modifiers applied
     * @return double
     */
    double getValue();

    /**
     * Get the default value of the attribute
     * @return double
     */
    double getDefaultValue();
}
