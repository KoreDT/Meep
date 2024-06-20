/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.player;

import net.kore.meep.api.entity.Player;
import net.kore.meep.api.world.World;

public class PlayerSwitchWorldEvent extends PlayerEvent {
    private World oldWorld;
    public World getOldWorld() {
        return oldWorld;
    }

    private World newWorld;
    public World getNewWorld() {
        return newWorld;
    }

    public PlayerSwitchWorldEvent(Player player, World oldWorld, World newWorld) {
        super(player);
        this.oldWorld = oldWorld;
        this.newWorld = newWorld;
    }
}
