/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.positioning;

public class Angle extends IAngle<Angle> {
    public Angle(double yaw, double pitch) {
        super(yaw, pitch);
    }

    @Override
    public Angle getThis() {
        return this;
    }

    @Override
    public Angle newThis(double yaw, double pitch) {
        return new Angle(yaw, pitch);
    }
}
