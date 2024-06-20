/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.item;

import net.kore.meep.api.enchant.Enchant;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ItemMeta {
    boolean hasName();
    Component getName();
    void setName(Component component);

    boolean hasLore();
    List<Component> getLore();
    void setLore(List<? extends Component> components);

    boolean hasCustomModelData();
    int getCustomModelData();
    void setCustomModelData(@Nullable Integer customModelData);

    boolean hasEnchant();
    boolean hasEnchant(@NotNull Enchant enchant);
    int getEnchantLevel(@NotNull Enchant enchant);
    @NotNull Map<Enchant, Integer> getEnchants();
    boolean addEnchant(@NotNull Enchant enchant, int level);
    boolean removeEnchant(@NotNull Enchant enchant);
    void removeAllEnchant();
    boolean willEnchantConflict(@NotNull Enchant enchant);

    void addItemFlags(ItemFlag... itemFlags);
    void removeItemFlags(ItemFlag... itemFlags);
    @NotNull Set<ItemFlag> getItemFlags();
    boolean hasItemFlag(@NotNull ItemFlag itemFlag);

    boolean isHideTooltip();
    void setHideTooltip(boolean hideTooltip);

    boolean isUnbreakable();
    void setUnbreakable(boolean unbreakable);

    boolean hasMaxStackSize();
    int getMaxStackSize();
    void setMaxStackSize(@Nullable Integer maxStackSize);

    ItemMeta clone();
}
