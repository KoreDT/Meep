/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.positioning;

public class Basic3DPoint extends Point3D<Basic3DPoint> {
    public Basic3DPoint(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    public Basic3DPoint getThis() {
        return this;
    }

    @Override
    public Basic3DPoint newThis(double x, double y, double z) {
        return new Basic3DPoint(x, y, z);
    }
}
