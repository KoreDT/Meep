/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.block;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.block.BlockDirection;
import net.kore.meep.api.positioning.WorldPosition;
import net.kore.meep.paper.world.World;
import org.bukkit.block.BlockFace;

public class Block implements net.kore.meep.api.block.Block {
    private org.bukkit.block.Block parent;

    public Block(org.bukkit.block.Block parent) {
        this.parent = parent;
    }

    @Override
    public NamespaceKey getKey() {
        return NamespaceKey.MINECRAFT(parent.getType().name().toLowerCase());
    }

    @Override
    public net.kore.meep.api.block.Block getRelative(BlockDirection direction, int distance) {
        BlockFace bf = switch (direction) {
            case NORTH -> BlockFace.NORTH;
            case EAST -> BlockFace.EAST;
            case SOUTH -> BlockFace.SOUTH;
            case WEST -> BlockFace.WEST;
            case UP -> BlockFace.UP;
            case DOWN -> BlockFace.DOWN;
        };
        return new Block(parent.getRelative(bf, distance));
    }

    @Override
    public WorldPosition getPosition() {
        return new WorldPosition(new World(parent.getWorld()), parent.getX(), parent.getY(), parent.getZ());
    }
}
