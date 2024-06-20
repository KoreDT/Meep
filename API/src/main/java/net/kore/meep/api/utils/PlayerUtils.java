/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.utils;

import net.kore.meep.api.entity.Player;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.positioning.Vector;

public class PlayerUtils {
    public static boolean isLookingAt(Player player, Player other) {
        Coordinates playerEye = player.getPosition().getCoordinates().addY(1.62);
        Coordinates entityEye = other.getPosition().getCoordinates().addY(1.62);
        Vector toEntity = Vector.from(entityEye).subtract(Vector.from(playerEye));
        double dot = toEntity.normalize().dot(Vector.from(player.getLookingAngle()));

        return dot > 0.99D;
    }
}
