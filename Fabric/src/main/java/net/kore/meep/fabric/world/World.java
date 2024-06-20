package net.kore.meep.fabric.world;

import net.kore.meep.api.block.Block;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.world.Chunk;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public class World implements net.kore.meep.api.world.World {
    private ServerLevel parent;

    public World(ServerLevel parent) {
        this.parent = parent;
    }

    @Override
    public int getEntityCount() {
        int count = 0;
        for (Entity e : parent.getEntities().getAll()) {
            count++;
        }
        return count;
    }

    @Override
    public int getTileEntityCount() {
        int count = 0;
        for (ChunkHolder chunk : parent.getChunkSource().chunkMap.visibleChunkMap.values()) {
            for (BlockEntity entity : chunk.getChunkToSend().getBlockEntities().values()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int getChunkCount() {
        return parent.getChunkSource().getLoadedChunksCount();
    }

    @Override
    public int getPlayerCount() {
        int count = 0;
        for (Entity e : parent.getEntities().getAll()) {
            if (e instanceof ServerPlayer) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Block getBlockAt(Coordinates coordinates) {
        return new net.kore.meep.fabric.block.Block(parent.getBlockState(new BlockPos((int) coordinates.getX(), (int) coordinates.getY(), (int) coordinates.getZ())).getBlock());
    }

    @Override
    public void setBlockAt(Coordinates coordinates, Block block) {
        ResourceLocation rl = new ResourceLocation(block.getKey().getNamespace(), block.getKey().getPath());
        parent.setBlock(new BlockPos((int) coordinates.getX(), (int) coordinates.getY(), (int) coordinates.getZ()), BuiltInRegistries.BLOCK.get(rl).defaultBlockState(), 0);
    }

    @Override
    public Chunk getChunk(int x, int z) {
        return new net.kore.meep.fabric.world.Chunk(parent.getChunk(x, z));
    }
}
