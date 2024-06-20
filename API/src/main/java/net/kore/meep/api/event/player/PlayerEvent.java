/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.player;

import net.kore.meep.api.event.Event;
import net.kore.meep.api.entity.Player;

public abstract class PlayerEvent extends Event {
    private Player player;
    public Player getPlayer() {
        return player;
    }

    public PlayerEvent(Player player) {
        this.player = player;
    }
}
