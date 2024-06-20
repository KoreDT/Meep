package net.kore.meep.fabric.item;

import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.item.ItemMeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class Item implements net.kore.meep.api.item.Item {
    private ItemStack parent;

    public Item(ItemStack parent) {
        this.parent = parent;
    }

    @Override
    public ItemMeta getItemMeta() {
        return null;
    }

    @Override
    public void setItemMeta(ItemMeta itemMeta) {

    }

    @Override
    public NamespaceKey getKey() {
        ResourceLocation rl = BuiltInRegistries.ITEM.getKey(parent.getItem());
        return new NamespaceKey(rl.getNamespace(), rl.getPath());
    }

    @Override
    public short getDamage() {
        return (short) parent.getDamageValue();
    }

    @Override
    public void setDamage(short damage) {
        parent.setDamageValue(damage);
    }

    @Override
    public net.kore.meep.api.item.Item clone() {
        return new Item(parent.copy());
    }
}
