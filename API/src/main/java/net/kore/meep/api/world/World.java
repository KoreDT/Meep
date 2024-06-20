/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.world;

import net.kore.meep.api.Keyable;
import net.kore.meep.api.block.Block;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.positioning.WorldPosition;

public interface World extends Keyable {
    int getEntityCount();
    int getTileEntityCount();
    int getChunkCount();
    int getPlayerCount();

    Block getBlockAt(Coordinates coordinates);
    void setBlockAt(Coordinates coordinates, Block block);

    default Block getBlockAt(WorldPosition position) {
        if (position.getWorld().equals(this)) {
            return getBlockAt(position.getCoordinates());
        } else {
            return null;
        }
    }
    default void setBlockAt(WorldPosition position, Block block) {
        if (position.getWorld().equals(this)) {
            setBlockAt(position.getCoordinates(), block);
        }
    }

    Chunk getChunk(int x, int z);
    default Chunk getChunk(Coordinates coordinates) {
        return getChunk((int) Math.floor(coordinates.getX() / 16), (int) Math.floor(coordinates.getZ() / 16));
    }
    default Chunk getChunk(WorldPosition position) {
        return getChunk(position.getCoordinates());
    }
    default Chunk getChunk(Block block) {
        return getChunk(block.getWorldPosition());
    }
}
