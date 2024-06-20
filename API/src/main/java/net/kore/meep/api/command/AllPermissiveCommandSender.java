/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command;

import net.kore.meep.api.permission.Permission;
import net.kore.meep.api.permission.PermissionManager;

import java.util.Set;

/**
 * A {@link CommandSender} with no permission checks
 */
public interface AllPermissiveCommandSender extends CommandSender {
    @Override
    default void addPermission(Permission permission) {}

    @Override
    default void removePermission(Permission permission) {}

    @Override
    default boolean hasPermission(Permission permission) {
        return true;
    }

    @Override
    default boolean hasPermission(String permission) {
        return true;
    }

    @Override
    default Set<Permission> getPermissions() {
        return PermissionManager.get().getPermissions();
    }
}
