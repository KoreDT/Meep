/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.block;

import net.kore.meep.api.block.Block;
import net.kore.meep.api.event.Event;

public abstract class BlockEvent extends Event {
    private final Block block;
    public Block getBlock() {
        return block;
    }

    public BlockEvent(Block block) {
        this.block = block;
    }
}
