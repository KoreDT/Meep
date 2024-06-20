/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.world;

import net.kore.meep.api.block.Block;
import net.kore.meep.api.world.World;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class Chunk implements net.kore.meep.api.world.Chunk {
    private org.bukkit.Chunk parent;

    public Chunk(org.bukkit.Chunk parent) {
        this.parent = parent;
    }

    @Override
    public int getX() {
        return parent.getX();
    }

    @Override
    public int getZ() {
        return parent.getZ();
    }

    @Override
    public World getWorld() {
        return new net.kore.meep.paper.world.World(parent.getWorld());
    }

    @Override
    public Block getBlock(@Range(from = 0, to = 15) int x, @Range(from = -64, to = 319) int y, @Range(from = 0, to = 15) int z) {
        return new net.kore.meep.paper.block.Block(parent.getBlock(x, y, z));
    }

    @Override
    public @Nullable String getPersistantString(@NotNull String path) {
        return parent.getPersistentDataContainer().get(new NamespacedKey("meep", path), PersistentDataType.STRING);
    }

    @Override
    public void setPersistantString(@NotNull String path, @Nullable String value) {
        if (value == null) parent.getPersistentDataContainer().remove(new NamespacedKey("meep", path));
        else parent.getPersistentDataContainer().set(new NamespacedKey("meep", path), PersistentDataType.STRING, value);
    }

    @Override
    public @Nullable Integer getPersistantInteger(@NotNull String path) {
        return parent.getPersistentDataContainer().get(new NamespacedKey("meep", path), PersistentDataType.INTEGER);
    }

    @Override
    public void setPersistantInteger(@NotNull String path, @Nullable Integer value) {
        if (value == null) parent.getPersistentDataContainer().remove(new NamespacedKey("meep", path));
        else parent.getPersistentDataContainer().set(new NamespacedKey("meep", path), PersistentDataType.INTEGER, value);
    }

    @Override
    public @Nullable Boolean getPersistantBoolean(@NotNull String path) {
        return parent.getPersistentDataContainer().get(new NamespacedKey("meep", path), PersistentDataType.BOOLEAN);
    }

    @Override
    public void setPersistantBoolean(@NotNull String path, @Nullable Boolean value) {
        if (value == null) parent.getPersistentDataContainer().remove(new NamespacedKey("meep", path));
        else parent.getPersistentDataContainer().set(new NamespacedKey("meep", path), PersistentDataType.BOOLEAN, value);
    }
}
