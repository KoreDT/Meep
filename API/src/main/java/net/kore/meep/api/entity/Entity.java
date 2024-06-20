/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity;

import net.kore.meep.api.positioning.*;
import net.kore.meep.api.Keyable;
import net.kore.meep.api.PersistantStorer;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.positioning.Coordinates;

import java.util.UUID;

public interface Entity extends PersistantStorer, Keyable, CommandSender {
    /**
     * Get the UUID of the entity
     * @return {@link UUID}
     */
    UUID getUUID();

    /**
     * Get the name of the entity
     * @return {@link String}
     */
    String getName();

    /**
     * Get the position of the entity
     * @return {@link WorldPosition}
     */
    @CannotReturnRelative
    WorldPosition getPosition();

    /**
     * Set the position of the entity
     * @param coordinates The position excluding the world
     */
    void setPosition(Coordinates coordinates);

    /**
     * Set the position of the entity
     * @param position The position including the world
     */
    void setPosition(WorldPosition position);

    /**
     * Get the current looking angle of the entity
     * @return {@link Angle}
     */
    Angle getLookingAngle();

    /**
     * Set the current looking angle of the entity
     * @param angle The angle for the entity to look
     */
    void setLookingAngle(Angle angle);

    /**
     * Get the current velocity of the entity
     * @return {@link Vector}
     */
    Vector getVelocity();

    /**
     * Set the current velocity of the entity
     * @param vector The velocity to set
     */
    void setVelocity(Vector vector);

    /**
     * Teleport the entity to a location including an angle
     * @param coordinates The place to be excluding world
     * @param angle The angle to look at
     */
    void teleport(Coordinates coordinates, Angle angle);

    /**
     * Teleport the entity to a location including an angle
     * @param position The place to be including world
     * @param angle The angle to look at
     */
    void teleport(WorldPosition position, Angle angle);

    /**
     * Ditto for {@link #setPosition(Coordinates)}
     * @param coordinates The position excluding the world
     */
    default void teleport(Coordinates coordinates) {
        setPosition(coordinates);
    }

    /**
     * Ditto for {@link #setPosition(WorldPosition)}
     * @param position The position including the world
     */
    default void teleport(WorldPosition position) {
        setPosition(position);
    }
}
