/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.world;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.block.Block;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.world.Chunk;

public class World implements net.kore.meep.api.world.World {
    public org.bukkit.World getParent() {
        return parent;
    }

    private org.bukkit.World parent;

    public World(org.bukkit.World parent) {
        this.parent = parent;
    }

    @Override
    public NamespaceKey getKey() {
        return new NamespaceKey(parent.getKey().namespace(), parent.getKey().value(), NamespaceKey.KeyType.WORLD);
    }

    @Override
    public int getEntityCount() {
        return parent.getEntityCount();
    }

    @Override
    public int getTileEntityCount() {
        return parent.getTileEntityCount();
    }

    @Override
    public int getChunkCount() {
        return parent.getChunkCount();
    }

    @Override
    public int getPlayerCount() {
        return parent.getPlayerCount();
    }

    @Override
    public Block getBlockAt(Coordinates coordinates) {
        return null;
    }

    @Override
    public void setBlockAt(Coordinates coordinates, Block block) {

    }

    @Override
    public Chunk getChunk(int x, int z) {
        return null;
    }
}
