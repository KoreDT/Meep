/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.permission;

import org.jetbrains.annotations.ApiStatus;

import java.util.Set;

public abstract class PermissionManager {
    private static PermissionManager INSTANCE;
    public static PermissionManager get() {
        if (INSTANCE == null) throw new IllegalStateException("PermissionManager is not ready yet.");
        return INSTANCE;
    }
    @ApiStatus.Internal
    public static void set(PermissionManager permissionManager) {
        INSTANCE = permissionManager;
    }

    public abstract void registerPermission(Permission permission);
    public abstract void unregisterPermission(Permission permission);
    public void registerPermissions(Permission... permissions) {
        for (Permission permission : permissions) {
            registerPermission(permission);
        }
    }
    public abstract Set<Permission> getPermissions();
}
