package net.kore.meep.fabric.block;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.block.BlockDirection;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.positioning.WorldPosition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class Block implements net.kore.meep.api.block.Block {
    private net.minecraft.world.level.block.Block parent;

    public Block (net.minecraft.world.level.block.Block parent) {
        this.parent = parent;
    }

    @Override
    public NamespaceKey getKey() {
        ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(parent);
        return new NamespaceKey(rl.getNamespace(), rl.getPath());
    }

    @Override
    public net.kore.meep.api.block.Block getRelative(BlockDirection direction, int distance) {
        Coordinates coordinatesToAdd = switch (direction) {
            case NORTH -> new Coordinates(0, 0, -1);
            case EAST -> new Coordinates(1, 0, 0);
            case SOUTH -> new Coordinates(0, 0, 1);
            case WEST -> new Coordinates(-1, 0, 0);
            case UP -> new Coordinates(0, 1, 0);
            case DOWN -> new Coordinates(0, -1, 0);
        };
        return new WorldPosition(getWorldPosition().getWorld(), coordinatesToAdd.add(getWorldPosition().getCoordinates())).getBlock();
    }

    @Override
    public WorldPosition getWorldPosition() {
        return null;
    }
}
