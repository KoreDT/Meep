/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.player;

import net.kore.meep.api.event.CancellableEvent;
import net.kore.meep.api.entity.Player;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.Set;

public class PlayerChatEvent extends PlayerEvent implements CancellableEvent {
    private Component message;
    private final Set<Audience> audience;
    private boolean cancelled;

    public PlayerChatEvent(Player player, Component message, Set<Audience> audience) {
        super(player);
        this.message = message;
        this.audience = audience;
    }

    @Override
    public void cancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }

    public Component getMessage() {
        return message;
    }
    public void setMessage(Component message) {
        this.message = message;
    }

    public Set<Audience> getAudience() {
        return audience;
    }
}
