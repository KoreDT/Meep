/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.positioning;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.kore.meep.api.world.World;

public class Coordinates extends Point3D<Coordinates> {
    private boolean relative = false;
    private RelativeType relativeType = RelativeType.NONE;
    public boolean isRelative() {
        return relative;
    }
    public RelativeType getRelativeType() {
        return relativeType;
    }
    public void setRelative(boolean relative, RelativeType type) {
        this.relative = relative;
        this.relativeType = type;
    }

    @Override
    public Coordinates getThis() {
        return this;
    }

    @Override
    public Coordinates newThis(double x, double y, double z) {
        return new Coordinates(x, y, z);
    }

    public Coordinates(RelativeData x, RelativeData y, RelativeData z) throws IllegalArgumentException {
        this(x.value, y.value, z.value);
        this.relative = true;
        if (x.type.equals(y.type) && y.type.equals(z.type)) {
            this.relativeType = x.type;
        } else {
            throw new IllegalArgumentException("Not all types are the same.");
        }
    }

    public Coordinates(double x, double y, double z) {
        super(x, y, z);
    }

    public Coordinates(int x, int y, int z) {
        this(x + 0.5, y + 0.5, z + 0.5);
    }

    @CanIgnoreReturnValue
    public Coordinates centerToBlock() {
        setX(Math.floor(getX()) + 0.5);
        setY(Math.floor(getY()) + 0.5);
        setZ(Math.floor(getZ()) + 0.5);
        return this;
    }

    public WorldPosition getWorldPosition(World world) {
        return new WorldPosition(world, this);
    }

    public record RelativeData(double value, RelativeType type) {}
    public enum RelativeType {
        POSITIONAL, // ~ ~ ~
        DIRECTIONAL, // ^ ^ ^
        NONE; // 0 0 0
    }

    public static Coordinates from(Point3D<?> thing) {
        return new Coordinates(thing.getX(), thing.getY(), thing.getZ());
    }
}
