/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.utils;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.block.Block;
import net.kore.meep.api.enchant.Enchant;
import net.kore.meep.api.permission.Permission;
import net.kore.meep.paper.world.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BukkitToMeep {
    public static net.kore.meep.api.entity.Player player(Player p) {
        return new net.kore.meep.paper.entity.Player(p);
    }
    public static net.kore.meep.api.world.World world(org.bukkit.World w) {
        return new World(w);
    }
    public static Block block(org.bukkit.block.Block b) {
        return new net.kore.meep.paper.block.Block(b);
    }
    public static Enchant enchant(Enchantment e) {
        return new Enchant(new NamespaceKey(e.getKey().namespace(), e.getKey().value()));
    }
    public static Permission permission(org.bukkit.permissions.Permission p) {
        Permission.Default defaultPermission = switch (p.getDefault()) {
            case TRUE -> Permission.Default.TRUE;
            case FALSE -> Permission.Default.FALSE;
            case OP -> Permission.Default.OP;
            case NOT_OP -> Permission.Default.NOTOP;
        };

        return new Permission(p.getName(), p.getDescription(), defaultPermission);
    }
}
