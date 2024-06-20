/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.utils;

import net.kore.meep.api.positioning.Angle;
import net.kore.meep.api.positioning.Basic3DPoint;
import net.kore.meep.api.positioning.Coordinates;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException("Places cannot be less than 0");

        return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    public static double getTrueDistance(Coordinates original, Angle angle, Coordinates relativeCoords) {
        if (relativeCoords.getRelativeType().equals(Coordinates.RelativeType.DIRECTIONAL)) {
            float f = (float) Math.cos((angle.getYaw() + 90.0F) * (float) (Math.PI / 180.0));
            float g = (float) Math.sin((angle.getYaw() + 90.0F) * (float) (Math.PI / 180.0));
            float h = (float) Math.cos(-angle.getPitch() * (float) (Math.PI / 180.0));
            float i = (float) Math.sin(-angle.getPitch() * (float) (Math.PI / 180.0));
            float j = (float) Math.cos((-angle.getPitch() + 90.0F) * (float) (Math.PI / 180.0));
            float k = (float) Math.sin((-angle.getPitch() + 90.0F) * (float) (Math.PI / 180.0));
            Basic3DPoint vec32 = new Basic3DPoint(f * h, i, g * h);
            Basic3DPoint vec33 = new Basic3DPoint(f * j, k, g * j);
            Basic3DPoint vec34 = vec32.cross(vec33).multiply(new Basic3DPoint(-1, -1, -1));
            double d = vec32.getX() * relativeCoords.getZ() + vec33.getX() * relativeCoords.getY() + vec34.getX() * relativeCoords.getX();
            double e = vec32.getY() * relativeCoords.getZ() + vec33.getY() * relativeCoords.getY() + vec34.getY() * relativeCoords.getX();
            double l = vec32.getZ() * relativeCoords.getZ() + vec33.getZ() * relativeCoords.getY() + vec34.getZ() * relativeCoords.getX();
            return original.getDistanceFrom(new Coordinates(original.getX() + d, original.getY() + e, original.getZ() + l));
        } else if (relativeCoords.getRelativeType().equals(Coordinates.RelativeType.POSITIONAL)) {
            return original.getDistanceFrom(original.clone().add(relativeCoords));
        } else {
            return original.getDistanceFrom(relativeCoords);
        }
    }
}
