/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.positioning;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.jetbrains.annotations.NotNull;

public abstract class IAngle<T extends IAngle<T>> {
    private double yaw;
    public double getYaw() {
        return yaw;
    }
    @CanIgnoreReturnValue
    public T setYaw(double yaw) {
        this.yaw = yaw;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T addYaw(double yaw) {
        this.yaw += yaw;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T subtractYaw(double yaw) {
        this.yaw -= yaw;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T multiplyYaw(double yaw) {
        this.yaw *= yaw;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T divideYaw(double yaw) {
        this.yaw /= yaw;
        return getThis();
    }

    private double pitch;
    public double getPitch() {
        return pitch;
    }
    @CanIgnoreReturnValue
    public T setPitch(double pitch) {
        this.pitch = pitch;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T addPitch(double pitch) {
        this.pitch += pitch;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T subtractPitch(double pitch) {
        this.pitch -= pitch;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T multiplyPitch(double pitch) {
        this.pitch *= pitch;
        return getThis();
    }
    @CanIgnoreReturnValue
    public T dividePitch(double pitch) {
        this.pitch /= pitch;
        return getThis();
    }

    public abstract T getThis();
    public abstract T newThis(double yaw, double pitch);

    public IAngle(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @CanIgnoreReturnValue
    public @NotNull T add(@NotNull IAngle<?> other) {
        this.yaw += other.getYaw();
        this.pitch += other.getPitch();
        return getThis();
    }

    @CanIgnoreReturnValue
    public @NotNull T subtract(@NotNull IAngle<?> other) {
        this.yaw -= other.getYaw();
        this.pitch -= other.getPitch();
        return getThis();
    }

    @CanIgnoreReturnValue
    public @NotNull T multiply(@NotNull IAngle<?> other) {
        this.yaw *= other.getYaw();
        this.pitch *= other.getPitch();
        return getThis();
    }

    @CanIgnoreReturnValue
    public @NotNull T divide(@NotNull IAngle<?> other) {
        this.yaw /= other.getYaw();
        this.pitch /= other.getPitch();
        return getThis();
    }

    @CanIgnoreReturnValue
    public T normalize() {
        double v = length();
        divide(newThis(v, v));
        return getThis();
    }

    public double length() {
        return Math.sqrt(Math.pow(yaw, 2) + Math.pow(pitch, 2));
    }

    public double dot(T other) {
        return this.yaw * other.getYaw() + this.pitch * other.getPitch();
    }

    public double getDistanceFrom(T other) {
        return Math.abs(Math.sqrt(
                Math.pow((this.yaw - other.getYaw()), 2) +
                        Math.pow((this.pitch - other.getPitch()), 2)
        ));
    }

    @Override
    public T clone() {
        return newThis(getYaw(), getPitch());
    }

    @Override
    public String toString() {
        return "%s{yaw=%s,pitch=%s}".formatted(getClass().getSimpleName(), yaw, pitch);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof IAngle<?> IAngle && IAngle.getThis().getClass().equals(obj.getClass())) {
            return this.yaw == IAngle.yaw && this.pitch == IAngle.pitch;
        }
        return false;
    }
}
