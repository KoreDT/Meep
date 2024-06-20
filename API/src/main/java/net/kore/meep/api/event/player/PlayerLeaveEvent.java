/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.player;

import net.kore.meep.api.entity.Player;
import net.kyori.adventure.text.Component;

public class PlayerLeaveEvent extends PlayerEvent {
    private Component leaveMessage;

    public PlayerLeaveEvent(Player player, Component leaveMessage) {
        super(player);
        this.leaveMessage = leaveMessage;
    }

    public Component getLeaveMessage() {
        return leaveMessage;
    }
    public void setLeaveMessage(Component leaveMessage) {
        this.leaveMessage = leaveMessage;
    }
}
