/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.positioning;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public abstract class Point3D<T extends Point3D<T>> {
    private double x;
    public double getX() {
        return x;
    }
    @CanIgnoreReturnValue
    public T setX(double x) {
        this.x = x;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T addX(double x) {
        this.x += x;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T subtractX(double x) {
        this.x -= x;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T multiplyX(double x) {
        this.x *= x;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T divideX(double x) {
        this.x /= x;
        return getThis();
    }

    private double y;
    public double getY() {
        return y;
    }
    @CanIgnoreReturnValue
    public T setY(double y) {
        this.y = y;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T addY(double y) {
        this.y += y;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T subtractY(double y) {
        this.y -= y;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T multiplyY(double y) {
        this.y *= y;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T divideY(double y) {
        this.y /= y;
        return getThis();
    }

    private double z;
    public double getZ() {
        return z;
    }
    @CanIgnoreReturnValue
    public T setZ(double z) {
        this.z = z;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T addZ(double z) {
        this.z += z;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T subtractZ(double z) {
        this.z -= z;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T multiplyZ(double z) {
        this.z *= z;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T divideZ(double z) {
        this.z /= z;
        return getThis();
    }

    public abstract T getThis();
    public abstract T newThis(double x, double y, double z);

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Number x, Number y, Number z) {
        this(x.doubleValue(), y.doubleValue(), z.doubleValue());
    }

    @CanIgnoreReturnValue
    public @NotNull T add(@NotNull T other) {
        this.x += other.getX();
        this.y += other.getY();
        this.z += other.getZ();
        return getThis();
    }

    @CanIgnoreReturnValue
    public @NotNull T subtract(@NotNull T other) {
        this.x -= other.getX();
        this.y -= other.getY();
        this.z -= other.getZ();
        return getThis();
    }

    @CanIgnoreReturnValue
    public @NotNull T multiply(@NotNull T other) {
        this.x *= other.getX();
        this.y *= other.getY();
        this.z *= other.getZ();
        return getThis();
    }

    @CanIgnoreReturnValue
    public @NotNull T divide(@NotNull T other) {
        this.x /= other.getX();
        this.y /= other.getY();
        this.z /= other.getZ();
        return getThis();
    }

    @CanIgnoreReturnValue
    public T add(Axis axis, double value) {
        switch (axis) {
            case X -> this.x += value;
            case Y -> this.y += value;
            case Z -> this.z += value;
        }
        return getThis();
    }

    @CanIgnoreReturnValue
    public T subtract(Axis axis, double value) {
        switch (axis) {
            case X -> this.x -= value;
            case Y -> this.y -= value;
            case Z -> this.z -= value;
        }
        return getThis();
    }

    @CanIgnoreReturnValue
    public T multiply(Axis axis, double value) {
        switch (axis) {
            case X -> this.x *= value;
            case Y -> this.y *= value;
            case Z -> this.z *= value;
        }
        return getThis();
    }

    @CanIgnoreReturnValue
    public T divide(Axis axis, double value) {
        switch (axis) {
            case X -> this.x /= value;
            case Y -> this.y /= value;
            case Z -> this.z /= value;
        }
        return getThis();
    }

    @CanIgnoreReturnValue
    public T normalize() {
        double v = length();
        divide(newThis(v, v, v));
        return getThis();
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public double dot(T other) {
        return this.x * other.getX() + this.y * other.getY() + this.z * other.getZ();
    }

    public T cross(T other) {
        return newThis(
                this.y * other.getZ() - this.z * other.getY(),
                this.z * other.getZ() - this.x * other.getZ(),
                this.x * other.getY() - this.y * other.getZ()
        );
    }

    public double getDistanceFrom(T other) {
        return Math.abs(Math.sqrt(
                Math.pow((this.x - other.getX()), 2) +
                        Math.pow((this.y - other.getY()), 2) +
                        Math.pow((this.z - other.getZ()), 2)
        ));
    }

    @Override
    public T clone() {
        return newThis(getX(), getY(), getZ());
    }

    @Override
    public String toString() {
        return "%s{x=%s,y=%s,z=%s}".formatted(getClass().getSimpleName(), x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Point3D<?> point3D && point3D.getThis().getClass().equals(obj.getClass())) {
            return this.x == point3D.x && this.y == point3D.y && this.z == point3D.z;
        }
        return false;
    }
}
