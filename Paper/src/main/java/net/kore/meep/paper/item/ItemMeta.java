/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.item;

import net.kore.meep.api.enchant.Enchant;
import net.kore.meep.api.item.ItemFlag;
import net.kore.meep.paper.utils.BukkitToMeep;
import net.kore.meep.paper.utils.MeepToBukkit;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemMeta implements net.kore.meep.api.item.ItemMeta {
    private org.bukkit.inventory.meta.ItemMeta parent;
    public org.bukkit.inventory.meta.ItemMeta getParent() {
        return parent;
    }

    public ItemMeta(org.bukkit.inventory.meta.ItemMeta parent) {
        this.parent = parent;
    }

    @Override
    public boolean hasName() {
        return parent.hasItemName();
    }

    @Override
    public Component getName() {
        return parent.itemName();
    }

    @Override
    public void setName(Component component) {
        parent.itemName(component);
    }

    @Override
    public boolean hasLore() {
        return parent.hasLore();
    }

    @Override
    public List<Component> getLore() {
        return parent.lore();
    }

    @Override
    public void setLore(List<? extends Component> components) {
        parent.lore(components);
    }

    @Override
    public boolean hasCustomModelData() {
        return parent.hasCustomModelData();
    }

    @Override
    public int getCustomModelData() {
        return parent.getCustomModelData();
    }

    @Override
    public void setCustomModelData(@Nullable Integer customModelData) {
        parent.setCustomModelData(customModelData);
    }

    @Override
    public boolean hasEnchant() {
        return parent.hasEnchants();
    }

    @Override
    public boolean hasEnchant(@NotNull Enchant enchant) {
        return parent.hasEnchant(MeepToBukkit.enchantment(enchant));
    }

    @Override
    public int getEnchantLevel(@NotNull Enchant enchant) {
        return parent.getEnchantLevel(MeepToBukkit.enchantment(enchant));
    }

    @Override
    public @NotNull Map<Enchant, Integer> getEnchants() {
        Map<Enchant, Integer> maps = new HashMap<>();
        parent.getEnchants().forEach((e, i) -> {
            maps.put(BukkitToMeep.enchant(e), i);
        });
        return maps;
    }

    @Override
    public boolean addEnchant(@NotNull Enchant enchant, int level) {
        return parent.addEnchant(MeepToBukkit.enchantment(enchant), level, true);
    }

    @Override
    public boolean removeEnchant(@NotNull Enchant enchant) {
        return parent.removeEnchant(MeepToBukkit.enchantment(enchant));
    }

    @Override
    public void removeAllEnchant() {
        parent.removeEnchantments();
    }

    @Override
    public boolean willEnchantConflict(@NotNull Enchant enchant) {
        return parent.hasConflictingEnchant(MeepToBukkit.enchantment(enchant));
    }

    @Override
    public void addItemFlags(ItemFlag... itemFlags) {
        for (ItemFlag itemFlag : itemFlags) {
            if (itemFlag.equals(ItemFlag.YES_FIRE_RESISTANT)) {
                parent.setFireResistant(true);
            } else if (itemFlag.equals(ItemFlag.NO_ENCHANT_GLINT)) {
                parent.setEnchantmentGlintOverride(false);
            } else {
                org.bukkit.inventory.ItemFlag bukkitItemFlag = switch (itemFlag) {
                    case NO_ENCHANTS -> org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
                    case NO_ATTRIBUTES -> org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;
                    case NO_UNBREAKABLE -> org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE;
                    case NO_DESTROYS -> org.bukkit.inventory.ItemFlag.HIDE_DESTROYS;
                    case NO_PLACED_ON -> org.bukkit.inventory.ItemFlag.HIDE_PLACED_ON;
                    case NO_DYE -> org.bukkit.inventory.ItemFlag.HIDE_DYE;
                    case NO_ARMOR_TRIM -> org.bukkit.inventory.ItemFlag.HIDE_ARMOR_TRIM;
                    default -> throw new IllegalStateException("Unexpected value: " + itemFlag);
                };
                parent.addItemFlags(bukkitItemFlag);
            }
        }
    }

    @Override
    public void removeItemFlags(ItemFlag... itemFlags) {
        for (ItemFlag itemFlag : itemFlags) {
            if (itemFlag.equals(ItemFlag.YES_FIRE_RESISTANT)) {
                parent.setFireResistant(false);
            } else if (itemFlag.equals(ItemFlag.NO_ENCHANT_GLINT)) {
                parent.setEnchantmentGlintOverride(true);
            } else {
                org.bukkit.inventory.ItemFlag bukkitItemFlag = switch (itemFlag) {
                    case NO_ENCHANTS -> org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
                    case NO_ATTRIBUTES -> org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;
                    case NO_UNBREAKABLE -> org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE;
                    case NO_DESTROYS -> org.bukkit.inventory.ItemFlag.HIDE_DESTROYS;
                    case NO_PLACED_ON -> org.bukkit.inventory.ItemFlag.HIDE_PLACED_ON;
                    case NO_DYE -> org.bukkit.inventory.ItemFlag.HIDE_DYE;
                    case NO_ARMOR_TRIM -> org.bukkit.inventory.ItemFlag.HIDE_ARMOR_TRIM;
                    default -> throw new IllegalStateException("Unexpected value: " + itemFlag);
                };
                parent.removeItemFlags(bukkitItemFlag);
            }
        }
    }

    @Override
    public @NotNull Set<ItemFlag> getItemFlags() {
        Set<ItemFlag> itemFlags = new HashSet<>();
        parent.getItemFlags().forEach(flag -> {
            ItemFlag itemFlag = switch(flag) {
                case HIDE_ENCHANTS, HIDE_STORED_ENCHANTS -> ItemFlag.NO_ENCHANTS;
                case HIDE_ATTRIBUTES -> ItemFlag.NO_ATTRIBUTES;
                case HIDE_UNBREAKABLE -> ItemFlag.NO_UNBREAKABLE;
                case HIDE_DESTROYS -> ItemFlag.NO_DESTROYS;
                case HIDE_PLACED_ON -> ItemFlag.NO_PLACED_ON;
                case HIDE_ADDITIONAL_TOOLTIP -> null;
                case HIDE_DYE -> ItemFlag.NO_DYE;
                case HIDE_ARMOR_TRIM -> ItemFlag.NO_ARMOR_TRIM;
            };
            if (itemFlag != null) {
                itemFlags.add(itemFlag);
            }
        });

        if (!parent.getEnchantmentGlintOverride()) {
            itemFlags.add(ItemFlag.NO_ENCHANT_GLINT);
        }
        if (parent.isFireResistant()) {
            itemFlags.add(ItemFlag.YES_FIRE_RESISTANT);
        }

        return itemFlags;
    }

    @Override
    public boolean hasItemFlag(@NotNull ItemFlag itemFlag) {
        if (itemFlag.equals(ItemFlag.NO_ENCHANT_GLINT)) {
            return !parent.getEnchantmentGlintOverride();
        } else if (itemFlag.equals(ItemFlag.YES_FIRE_RESISTANT)) {
            return parent.isFireResistant();
        }

        org.bukkit.inventory.ItemFlag bukkitItemFlag = switch (itemFlag) {
            case NO_ENCHANTS -> org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;
            case NO_ATTRIBUTES -> org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES;
            case NO_UNBREAKABLE -> org.bukkit.inventory.ItemFlag.HIDE_UNBREAKABLE;
            case NO_DESTROYS -> org.bukkit.inventory.ItemFlag.HIDE_DESTROYS;
            case NO_PLACED_ON -> org.bukkit.inventory.ItemFlag.HIDE_PLACED_ON;
            case NO_DYE -> org.bukkit.inventory.ItemFlag.HIDE_DYE;
            case NO_ARMOR_TRIM -> org.bukkit.inventory.ItemFlag.HIDE_ARMOR_TRIM;
            default -> throw new IllegalStateException("Unexpected value: " + itemFlag);
        };
        return parent.hasItemFlag(bukkitItemFlag);
    }

    @Override
    public boolean isHideTooltip() {
        return parent.isHideTooltip();
    }

    @Override
    public void setHideTooltip(boolean hideTooltip) {
        parent.setHideTooltip(hideTooltip);
    }

    @Override
    public boolean isUnbreakable() {
        return parent.isUnbreakable();
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        parent.setUnbreakable(unbreakable);
    }

    @Override
    public boolean hasMaxStackSize() {
        return parent.hasMaxStackSize();
    }

    @Override
    public int getMaxStackSize() {
        return parent.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(@Nullable Integer maxStackSize) {
        parent.setMaxStackSize(maxStackSize);
    }

    @Override
    public net.kore.meep.api.item.ItemMeta clone() {
        return new ItemMeta(parent.clone());
    }
}
