/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.utils;

import net.kore.meep.api.enchant.Enchant;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.lang.reflect.InvocationTargetException;

public class MeepToBukkit {
    public static Enchantment enchantment(Enchant enchant) {
        try {
            Object obj = Enchantment.class.getDeclaredMethod("getEnchantment", String.class).invoke(null, enchant.key().getPath());
            if (obj instanceof Enchantment e) {
                return e;
            }
            return null;
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    public static Permission permission(net.kore.meep.api.permission.Permission permission) {
        PermissionDefault pd = switch (permission.opDefault()) {
            case TRUE -> PermissionDefault.TRUE;
            case FALSE -> PermissionDefault.FALSE;
            case OP -> PermissionDefault.OP;
            case NOTOP -> PermissionDefault.NOT_OP;
        };
        return new org.bukkit.permissions.Permission(permission.node(), permission.description(), pd);
    }
}
