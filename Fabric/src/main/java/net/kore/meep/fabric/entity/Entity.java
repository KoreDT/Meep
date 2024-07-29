package net.kore.meep.fabric.entity;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.permission.Permission;
import net.kore.meep.api.positioning.Angle;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.positioning.Vector;
import net.kore.meep.api.positioning.WorldPosition;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class Entity implements net.kore.meep.api.entity.Entity {
    private net.minecraft.world.entity.Entity parent;

    public Entity(net.minecraft.world.entity.Entity parent) {
        this.parent = parent;
    }

    @Override
    public NamespaceKey getKey() {
        ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(parent.getType());
        return new NamespaceKey(rl.getNamespace(), rl.getPath());
    }

    @Override
    public UUID getUUID() {
        return parent.getUUID();
    }

    @Override
    public String getName() {
        return parent.getName().getString();
    }

    @Override
    public WorldPosition getPosition() {
        return null;
    }

    @Override
    public void setPosition(Coordinates coordinates) {

    }

    @Override
    public void setPosition(WorldPosition position) {

    }

    @Override
    public Angle getLookingAngle() {
        return null;
    }

    @Override
    public void setLookingAngle(Angle angle) {

    }

    @Override
    public Vector getVelocity() {
        return null;
    }

    @Override
    public void setVelocity(Vector vector) {

    }

    @Override
    public void teleport(Coordinates coordinates, Angle angle) {

    }

    @Override
    public void teleport(WorldPosition position, Angle angle) {

    }

    @Override
    public String getPersistantString(@NotNull String path) {
        return "";
    }

    @Override
    public void setPersistantString(@NotNull String path, String value) {

    }

    @Override
    public @Nullable Integer getPersistantInteger(@NotNull String path) {
        return 0;
    }

    @Override
    public void setPersistantInteger(@NotNull String path, @Nullable Integer value) {

    }

    @Override
    public @Nullable Boolean getPersistantBoolean(@NotNull String path) {
        return true;
    }

    @Override
    public void setPersistantBoolean(@NotNull String path, @Nullable Boolean value) {

    }

    @Override
    public void addPermission(Permission permission) {

    }

    @Override
    public void removePermission(Permission permission) {

    }

    @Override
    public boolean hasPermission(Permission permission) {
        return false;
    }

    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public Set<Permission> getPermissions() {
        return Set.of();
    }
}
