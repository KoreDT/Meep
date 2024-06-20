/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity;

import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.permission.Permissible;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public interface Player extends Entity, Identity {
    /**
     * Get the Identity of the player
     * @return {@link Identity}
     */
    default @NotNull Identity identity() {
        return Identity.identity(getUUID());
    }

    /**
     * Get the display name of the player
     * @return {@link Component}
     */
    Component displayName();

    /**
     * Set the display name of the player
     * @param name The display name
     */
    void displayName(Component name);

    /**
     * Kick the player
     */
    void kick();

    /**
     * Kick the player with a reason
     * @param reason The reason
     */
    void kick(Component reason);

    /**
     * Ban the player
     */
    void ban();

    /**
     * Ban the player with a reason
     * @param reason The reason
     */
    void ban(Component reason);

    /**
     * Get if the player is sneaking
     * @return boolean
     */
    boolean isSneaking();

    /**
     * Get if the player is sprinting
     * @return boolean
     */
    boolean isSprinting();
}
