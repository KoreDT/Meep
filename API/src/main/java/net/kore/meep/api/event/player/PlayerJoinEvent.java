/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.player;

import net.kore.meep.api.entity.Player;
import net.kyori.adventure.text.Component;

public class PlayerJoinEvent extends PlayerEvent {
    private Component joinMessage;

    public PlayerJoinEvent(Player player, Component joinMessage) {
        super(player);
        this.joinMessage = joinMessage;
    }

    public Component getJoinMessage() {
        return joinMessage;
    }
    public void setJoinMessage(Component joinMessage) {
        this.joinMessage = joinMessage;
    }
}
