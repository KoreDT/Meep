package net.kore.meep.fabric;

import net.kore.meep.api.Console;
import net.kore.meep.api.Meep;
import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.WhitelistManager;
import net.kore.meep.api.command.MeepCommandUtils;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.packets.PacketListener;
import net.kore.meep.api.task.TaskSchedular;
import net.kore.meep.api.world.World;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FabricyMeep extends Meep {
    private final FabricWhitelistManager manager = new FabricWhitelistManager();
    private final FabricConsole console = new FabricConsole();

    @Override
    public void executeCrash(@Nullable Throwable t, String message) {

    }

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
    public List<World> getWorlds() {
        return List.of();
    }

    @Override
    public List<NamespaceKey> getKnownBlockKeys() {
        List<NamespaceKey> blocks = new ArrayList<>();
        BuiltInRegistries.BLOCK.forEach(b -> {
            ResourceLocation rl = BuiltInRegistries.BLOCK.getKey(b);
            blocks.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.BLOCK));
        });
        return blocks;
    }

    @Override
    public List<NamespaceKey> getKnownItemKeys() {
        List<NamespaceKey> items = new ArrayList<>();
        BuiltInRegistries.ITEM.forEach(i -> {
            ResourceLocation rl = BuiltInRegistries.ITEM.getKey(i);
            items.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.ITEM));
        });
        return items;
    }

    @Override
    public List<NamespaceKey> getKnownParticleKeys() {
        return List.of();
    }

    @Override
    public List<NamespaceKey> getKnownVillagerTypeKeys() {
        return List.of();
    }

    @Override
    public List<NamespaceKey> getKnownVillagerProfessionKeys() {
        return List.of();
    }

    @Override
    public List<NamespaceKey> getKnownEntityKeys() {
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

    @Override
    public TaskSchedular getSchedular() {
        return null;
    }

    @Override
    public TaskSchedular getAsyncSchedular() {
        return null;
    }

    @Override
    public MeepCommandUtils getCommandUtils() {
        return null;
    }

    @Override
    public void registerIncomingPackets(String channel, PacketListener packetListener) {

    }

    @Override
    public void registerOutgoingPackets(String channel) {

    }

    @Override
    public int getTPS() {
        return 0;
    }
}
