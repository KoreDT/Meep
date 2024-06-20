/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.block;

import net.kore.meep.api.Keyable;
import net.kore.meep.api.positioning.WorldPosition;
import net.kore.meep.api.world.Chunk;

public interface Block extends Keyable {
    /**
     * Get a block relative to the current
     * @param direction The direction of which to get the block
     * @param distance How far is the block
     * @return {@link Block}
     */
    Block getRelative(BlockDirection direction, int distance);

    /**
     * Get the block above the current, see {@link #getRelative(BlockDirection,int) getRelative}
     * @return {@link Block}
     */
    default Block getAbove() {
        return getRelative(BlockDirection.UP, 1);
    }

    /**
     * Get the block below the current, see {@link #getRelative(BlockDirection,int) getRelative}
     * @return {@link Block}
     */
    default Block getBelow() {
        return getRelative(BlockDirection.DOWN, 1);
    }

    /**
     * Get the world position of the current block
     * @return {@link WorldPosition}
     */
    WorldPosition getWorldPosition();

    /**
     * Get the chunk the block is in
     * @return {@link Chunk}
     */
    default Chunk getChunk() {
        return getWorldPosition().getChunk();
    }

    /**
     * Block data
     */
    class Data {
        //TODO: Block Data
    }
}
