package net.kore.meep.fabric.world;

import net.kore.meep.api.block.Block;
import net.kore.meep.api.world.World;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class Chunk implements net.kore.meep.api.world.Chunk {
    private LevelChunk parent;

    public Chunk(LevelChunk parent) {
        this.parent = parent;
    }

    @Override
    public int getX() {
        return parent.getPos().x;
    }

    @Override
    public int getZ() {
        return parent.getPos().z;
    }

    @Override
    public World getWorld() {
        return new net.kore.meep.fabric.world.World((ServerLevel) parent.getLevel());
    }

    @Override
    public Block getBlock(@Range(from = 0, to = 15) int x, @Range(from = -64, to = 319) int y, @Range(from = 0, to = 15) int z) {
        return new net.kore.meep.fabric.block.Block(parent.getBlockState(new BlockPos(x, y, z)).getBlock());
    }

    @Override
    public String getPersistantString(@NotNull String path) {
        return "";
    }

    @Override
    public void setPersistantString(@NotNull String path, String value) {

    }
}
