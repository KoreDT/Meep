/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command;

import net.kore.meep.api.block.Block;

public class CommandBlockSender implements AllPermissiveCommandSender {
    private final Block block;

    /**
     * Get the block which executed the command
     * @return {@link Block}
     */
    public Block getBlock() {
        return block;
    }
    public CommandBlockSender(Block block) {
        this.block = block;
    }
}
