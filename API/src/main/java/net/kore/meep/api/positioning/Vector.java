/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.positioning;

public class Vector extends Point3D<Vector> {
    public static Vector from(Angle angle) {
        double rotX = angle.getYaw();
        double rotY = angle.getPitch();
        double xz = Math.cos(Math.toRadians(rotY));
        return new Vector(-xz * Math.sin(Math.toRadians(rotX)), -Math.sin(Math.toRadians(rotY)), xz * Math.cos(Math.toRadians(rotX)));
    }

    @Override
    public Vector getThis() {
        return this;
    }

    @Override
    public Vector newThis(double x, double y, double z) {
        return new Vector(x, y, z);
    }

    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    public static Vector none() {
        return new Vector(0, 0, 0);
    }
    public static Vector from(Point3D<?> thing) {
        return new Vector(thing.getX(), thing.getY(), thing.getZ());
    }
}
