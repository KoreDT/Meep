/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.world;

import net.kore.meep.api.world.World;

public class WorldUnloadEvent extends WorldEvent {
    public WorldUnloadEvent(World world) {
        super(world);
    }
}
