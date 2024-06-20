package net.kore.meep.fabric.item;

import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.enchant.Enchant;
import net.kore.meep.api.item.ItemFlag;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.text.Component;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ItemMeta implements net.kore.meep.api.item.ItemMeta {
    private ItemStack parent;

    public ItemMeta(ItemStack parent) {
        this.parent = parent;
    }

    @Override
    public boolean hasName() {
        return parent.has(DataComponents.CUSTOM_NAME);
    }

    @Override
    public Component getName() {
        return FabricAudiences.nonWrappingSerializer().deserialize(parent.getDisplayName());
    }

    @Override
    public void setName(Component component) {
        parent.set(DataComponents.CUSTOM_NAME, FabricAudiences.nonWrappingSerializer().serialize(component));
    }

    @Override
    public boolean hasLore() {
        return parent.has(DataComponents.LORE);
    }

    @Override
    public List<Component> getLore() {
        if (parent.get(DataComponents.LORE) == null) {
            return Collections.emptyList();
        }
        List<Component> components = new ArrayList<>();
        for (net.minecraft.network.chat.Component c : parent.get(DataComponents.LORE).lines()) {
            components.add(FabricAudiences.nonWrappingSerializer().deserialize(c));
        }
        return components;
    }

    @Override
    public void setLore(List<? extends Component> components) {
        List<net.minecraft.network.chat.Component> cs = new ArrayList<>();
        for (Component c : components) {
            cs.add(FabricAudiences.nonWrappingSerializer().serialize(c));
        }
        parent.set(DataComponents.LORE, new ItemLore(cs));
    }

    @Override
    public boolean hasCustomModelData() {
        return parent.get(DataComponents.CUSTOM_MODEL_DATA) != null;
    }

    @Override
    public int getCustomModelData() {
        return parent.get(DataComponents.CUSTOM_MODEL_DATA).value();
    }

    @Override
    public void setCustomModelData(@Nullable Integer customModelData) {
        if (customModelData == null) {
            parent.remove(DataComponents.CUSTOM_MODEL_DATA);
        } else {
            parent.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(customModelData));
        }
    }

    @Override
    public boolean hasEnchant() {
        return parent.isEnchanted();
    }

    @Override
    public boolean hasEnchant(@NotNull Enchant enchant) {
        for (Holder<Enchantment> enchantments : parent.getEnchantments().keySet()) {
            ResourceLocation rl = BuiltInRegistries.ENCHANTMENT.getKey(enchantments.value());
            if (rl.getNamespace().equals(enchant.key().getNamespace()) && rl.getPath().equals(enchant.key().getPath())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getEnchantLevel(@NotNull Enchant enchant) {
        Enchantment e = BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(enchant.key().getNamespace(), enchant.key().getPath()));
        return parent.getEnchantments().getLevel(e);
    }

    @Override
    public @NotNull Map<Enchant, Integer> getEnchants() {
        Map<Enchant, Integer> m = new HashMap<>();
        for (Holder<Enchantment> e : parent.getEnchantments().keySet()) {
            ResourceLocation rl = BuiltInRegistries.ENCHANTMENT.getKey(e.value());
            m.put(new Enchant(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.ENCHANT)), parent.getEnchantments().getLevel(e.value()));
        }
        return m;
    }

    @Override
    public boolean addEnchant(@NotNull Enchant enchant, int level) {
        parent.enchant(BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(enchant.key().getNamespace(), enchant.key().getPath())), level);
        return true;
    }

    @Override
    public boolean removeEnchant(@NotNull Enchant enchant) {
        return addEnchant(enchant, 0);
    }

    @Override
    public void removeAllEnchant() {
        for (Holder<Enchantment> e : parent.getEnchantments().keySet()) {
            ResourceLocation rl = BuiltInRegistries.ENCHANTMENT.getKey(e.value());
            addEnchant(new Enchant(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.ENCHANT)), 0);
        }
    }

    @Override
    public boolean willEnchantConflict(@NotNull Enchant enchant) {
        return !parent.canBeEnchantedWith(Holder.direct(BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(enchant.key().getNamespace(), enchant.key().getPath()))), EnchantingContext.PRIMARY);
    }

    @Override
    public void addItemFlags(ItemFlag... itemFlags) {
        for (ItemFlag itemFlag : itemFlags) {
            switch (itemFlag) {
                case YES_FIRE_RESISTANT -> parent.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
                case NO_ENCHANT_GLINT -> parent.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
                case NO_ARMOR_TRIM -> {
                    ArmorTrim at = parent.get(DataComponents.TRIM);
                    if (at == null) {
                        parent.set(DataComponents.TRIM, new ArmorTrim(null, null, false));
                    } else {
                        parent.set(DataComponents.TRIM, new ArmorTrim(at.material(), at.pattern(), false));
                    }
                }
            };
        }
    }

    @Override
    public void removeItemFlags(ItemFlag... itemFlags) {

    }

    @Override
    public @NotNull Set<ItemFlag> getItemFlags() {
        return Set.of();
    }

    @Override
    public boolean hasItemFlag(@NotNull ItemFlag itemFlag) {
        return false;
    }

    @Override
    public boolean isHideTooltip() {
        return false;
    }

    @Override
    public void setHideTooltip(boolean hideTooltip) {

    }

    @Override
    public boolean isUnbreakable() {
        return parent.has(DataComponents.UNBREAKABLE);
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        parent.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
    }

    @Override
    public boolean hasMaxStackSize() {
        return parent.has(DataComponents.MAX_STACK_SIZE);
    }

    @Override
    public int getMaxStackSize() {
        if (!hasMaxStackSize()) return 64;
        return parent.get(DataComponents.MAX_STACK_SIZE);
    }

    @Override
    public void setMaxStackSize(@Nullable Integer maxStackSize) {
        parent.set(DataComponents.MAX_STACK_SIZE, maxStackSize);
    }

    @Override
    public net.kore.meep.api.item.ItemMeta clone() {
        return new ItemMeta(parent.copy());
    }
}
