/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.world;

import net.kore.meep.api.event.Event;
import net.kore.meep.api.world.World;

public abstract class WorldEvent extends Event {
    private final World world;

    /**
     * Get the world with this event
     * @return {@link World}
     */
    public World getWorld() {
        return world;
    }

    public WorldEvent(World world) {
        this.world = world;
    }
}
