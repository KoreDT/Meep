/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.permission;

import net.kore.meep.api.permission.Permission;
import net.kore.meep.api.permission.PermissionManager;
import net.kore.meep.paper.utils.BukkitToMeep;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;

import java.util.HashSet;
import java.util.Set;

public class PaperPermissionManager extends PermissionManager {
    @Override
    public void registerPermission(Permission permission) {
        PermissionDefault pd = switch (permission.opDefault()) {
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
            case OP -> PermissionDefault.OP;
            case NOTOP -> PermissionDefault.NOT_OP;
        };
        Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission(permission.node(), permission.description(), pd));
    }

    @Override
    public void unregisterPermission(Permission permission) {
        PermissionDefault pd = switch (permission.opDefault()) {
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
            case OP -> PermissionDefault.OP;
            case NOTOP -> PermissionDefault.NOT_OP;
        };
        Bukkit.getPluginManager().removePermission(new org.bukkit.permissions.Permission(permission.node(), permission.description(), pd));
    }

    @Override
    public Set<Permission> getPermissions() {
        Set<Permission> set = new HashSet<>();
        for (org.bukkit.permissions.Permission p : Bukkit.getPluginManager().getPermissions()) {
            set.add(BukkitToMeep.permission(p));
        }
        return set;
    }
}
