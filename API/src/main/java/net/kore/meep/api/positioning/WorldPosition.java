/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.positioning;

import net.kore.meep.api.block.Block;
import net.kore.meep.api.world.Chunk;
import net.kore.meep.api.world.World;

public class WorldPosition {
    private World world;
    public World getWorld() {
        return world;
    }
    public void setWorld(World world) {
        this.world = world;
    }

    private Coordinates coordinates;
    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setPosition(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public WorldPosition(World world, Coordinates coordinates) {
        this.world = world;
        this.coordinates = coordinates;
    }

    public Block getBlock() {
        return world.getBlockAt(coordinates);
    }

    public void setBlock(Block block) {
        world.setBlockAt(this, block);
    }

    public Chunk getChunk() {
        return world.getChunk((int) Math.floor(coordinates.getX() / 16), (int) Math.floor(coordinates.getZ() / 16));
    }

    @Override
    public WorldPosition clone() {
        return new WorldPosition(world, coordinates);
    }

    @Override
    public String toString() {
        return "WorldPosition{x=%s,y=%s,z=%s}".formatted(coordinates.getX(), coordinates.getY(), coordinates.getZ());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof WorldPosition that) {
            return this.coordinates.getX() == that.coordinates.getX() && this.coordinates.getY() == that.coordinates.getY() && this.coordinates.getZ() == that.coordinates.getZ() && this.getWorld().equals(that.getWorld());
        }
        return false;
    }
}
