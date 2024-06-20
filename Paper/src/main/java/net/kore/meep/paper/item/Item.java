/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.item;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.item.ItemMeta;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class Item implements net.kore.meep.api.item.Item {
    private ItemStack parent;

    public Item(ItemStack parent) {
        this.parent = parent;
    }

    @Override
    public ItemMeta getItemMeta() {
        return new net.kore.meep.paper.item.ItemMeta(parent.getItemMeta());
    }

    @Override
    public void setItemMeta(ItemMeta itemMeta) {
        if (itemMeta instanceof net.kore.meep.paper.item.ItemMeta im) {
            parent.setItemMeta(im.getParent());
        }
    }

    @Override
    public NamespaceKey getKey() {
        NamespacedKey nk = parent.getType().getKey();
        return new NamespaceKey(nk.namespace(), nk.value());
    }

    @Override
    public short getDamage() {
        if (parent.getItemMeta() instanceof Damageable damage) {
            return (short) damage.getDamage();
        }
        return 0;
    }

    @Override
    public void setDamage(short damage) {
        if (parent.getItemMeta() instanceof Damageable damageable) {
            damageable.setDamage(damage);
        }
    }

    @Override
    public net.kore.meep.api.item.Item clone() {
        return new Item(parent.clone());
    }
}
