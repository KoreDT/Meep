/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.block;

import net.kore.meep.api.block.Block;
import net.kore.meep.api.event.CancellableEvent;
import net.kore.meep.api.entity.Player;

public class BlockBreakEvent extends BlockEvent implements CancellableEvent {
    private final Player player;
    public Player getPlayer() {
        return player;
    }

    private boolean cancelled;

    public BlockBreakEvent(Block block, Player player) {
        super(block);
        this.player = player;
    }

    @Override
    public void cancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }
}
