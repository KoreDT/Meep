package net.kore.meep.fabric;

import net.kore.meep.api.Console;
import net.kore.meep.api.Meep;
import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.WhitelistManager;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.world.World;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FabricyMeep extends Meep {
    private final FabricWhitelistManager manager = new FabricWhitelistManager();
    private final FabricConsole console = new FabricConsole();

    @Override
    public List<Player> getOnlinePlayers() {
        return List.of();
    }

    @Override
    public Player getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public World getWorld(NamespaceKey key) {
        return null;
    }

    @Override
    public List<NamespaceKey> getUsableBlockKeys() {
        List<NamespaceKey> blocks = new ArrayList<>();
        BuiltInRegistries.BLOCK.forEach(b -> {
            ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(b);
            blocks.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.BLOCK));
        });
        return blocks;
    }

    @Override
    public List<NamespaceKey> getUsableItemKeys() {
        List<NamespaceKey> items = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(i -> {
            ResourceLocation rl = BuiltInRegistries.ITEM.getKey(i);
            items.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.ITEM));
        });
        return items;
    }

    @Override
    public List<NamespaceKey> getUsableEnchantKeys() {
        List<NamespaceKey> enchants = new ArrayList<>();
        BuiltInRegistries.ENCHANTMENT.forEach(e -> {
            ResourceLocation rl = BuiltInRegistries.ENCHANTMENT.getKey(e);
            if (rl != null) {
                enchants.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.ENCHANT));
            }
        });
        return enchants;
    }

    @Override
    public List<NamespaceKey> getUsableEntityKeys() {
        List<NamespaceKey> entities = new ArrayList<>();
        BuiltInRegistries.ENTITY_TYPE.forEach(e -> {
            ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(e);
            entities.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.ENTITY));
        });
        return entities;
    }

    @Override
    public WhitelistManager getWhitelistManager() {
        return manager;
    }

    @Override
    public Console getConsole() {
        return console;
    }
}
