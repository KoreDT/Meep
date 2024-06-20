/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.entity;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.command.AllPermissiveCommandSender;
import net.kore.meep.api.positioning.*;
import net.kore.meep.paper.world.World;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class Entity implements net.kore.meep.api.entity.Entity, AllPermissiveCommandSender {
    private final org.bukkit.entity.Entity parent;
    public org.bukkit.entity.Entity getParent() {
        return parent;
    }

    public Entity(org.bukkit.entity.Entity parent) {
        this.parent = parent;
    }

    @Override
    public NamespaceKey getKey() {
        NamespacedKey nk = parent.getType().getKey();
        return new NamespaceKey(nk.namespace(), nk.value());
    }

    @Override
    public UUID getUUID() {
        return parent.getUniqueId();
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public WorldPosition getPosition() {
        return new WorldPosition(new World(parent.getWorld()), new Coordinates(parent.getX(), parent.getY(), parent.getZ()));
    }

    @Override
    public void setPosition(Coordinates coordinates) {
        parent.teleport(new Location(parent.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ()));
    }

    @Override
    public void setPosition(WorldPosition position) {
        parent.teleport(new Location( ((World)position.getWorld()).getParent(), position.getPosition().getX(), position.getPosition().getY(), position.getPosition().getZ()));
    }

    @Override
    public Angle getLookingAngle() {
        return new Angle(parent.getYaw(), parent.getPitch());
    }

    @Override
    public void setLookingAngle(Angle angle) {
        parent.setRotation((float) angle.getYaw(), (float) angle.getPitch());
    }

    @Override
    public Vector getVelocity() {
        return new Vector(parent.getVelocity().getX(), parent.getVelocity().getY(), parent.getVelocity().getZ());
    }

    @Override
    public void setVelocity(Vector vector) {
        parent.setVelocity(new org.bukkit.util.Vector(vector.getX(), vector.getY(), vector.getZ()));
    }

    @Override
    public void teleport(Coordinates coordinates, Angle angle) {
        parent.teleport(new Location(parent.getWorld(), coordinates.getX(), coordinates.getY(), coordinates.getZ(), (float) angle.getYaw(), (float) angle.getPitch()));
    }

    @Override
    public void teleport(WorldPosition position, Angle angle) {
        parent.teleport(new Location( ((World)position.getWorld()).getParent(), position.getPosition().getX(), position.getPosition().getY(), position.getPosition().getZ(), (float) angle.getYaw(), (float) angle.getPitch()));
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
