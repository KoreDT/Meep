/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.utils;

import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.kore.meep.api.command.CommandBlockSender;
import net.kore.meep.api.command.CommandProxySender;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.entity.Entity;
import net.kore.meep.api.positioning.Angle;
import net.kore.meep.api.positioning.CannotReturnRelative;
import net.kore.meep.api.positioning.Coordinates;
import org.jetbrains.annotations.Nullable;

public class CommandUtils {
    public static boolean canGetPosition(CommandSender sender) {
        return getPosition(sender) != null;
    }

    @CannotReturnRelative
    public static @Nullable Coordinates getPosition(CommandSender sender) {
        if (sender instanceof Entity entity) return entity.getPosition().getCoordinates();
        else if (sender instanceof CommandBlockSender block) return block.getBlock().getWorldPosition().getCoordinates();
        else if (sender instanceof CommandProxySender proxy) return getPosition(proxy.callee());
        return null;
    }

    @CannotReturnRelative
    public static Angle getAngle(CommandSender sender) {
        if (sender instanceof Entity entity) return entity.getLookingAngle();
        else if (sender instanceof CommandProxySender proxy) return getAngle(proxy.callee());
        return new Angle(0, 0);
    }

    public static CommandSyntaxException createException(String message) {
        return new CommandSyntaxException(new CommandExceptionType() {}, () -> message);
    }
}
