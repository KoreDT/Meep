/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper;

import com.google.common.collect.ImmutableList;
import net.kore.meep.api.Console;
import net.kore.meep.api.Meep;
import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.WhitelistManager;
import net.kore.meep.api.command.MeepCommandUtils;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.packets.PacketListener;
import net.kore.meep.api.task.TaskSchedular;
import net.kore.meep.api.world.World;
import net.kore.meep.paper.task.PaperAsyncTaskSchedular;
import net.kore.meep.paper.task.PaperTaskSchedular;
import net.kore.meep.paper.utils.BukkitToMeep;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PaperyMeep extends Meep {
    private final PaperWhitelistManager whitelistManager = new PaperWhitelistManager();
    private final PaperConsole console = new PaperConsole();
    private final PaperTaskSchedular taskSchedular = new PaperTaskSchedular();
    private final PaperAsyncTaskSchedular asyncTaskSchedular = new PaperAsyncTaskSchedular();
    private final PaperMeepCommandUtils commandUtils = new PaperMeepCommandUtils();

    @Override
    public void executeCrash(@Nullable Throwable t, String message) {
        MeepPaper.getPlugin(MeepPaper.class).getLogger().severe("Meep has detected an issue! The issue is: "+message + " | The server will now attempt to shutdown in a safe manner");
        if (t != null) {
            MinecraftServer.LOGGER.error("A provided error for Meep was found:", t);
        }

        MinecraftServer.getServer().stopServer();
    }

    @Override
    public List<Player> getOnlinePlayers() {
        List<Player> lp = new ArrayList<>();
        for (org.bukkit.entity.Player p : ImmutableList.copyOf(Bukkit.getOnlinePlayers())) {
            lp.add(new net.kore.meep.paper.entity.Player(p));
        }
        return ImmutableList.copyOf(lp);
    }

    @Override
    public Player getPlayer(UUID uuid) {
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        if (op.getPlayer() == null) {
            return null;
        } else {
            return new net.kore.meep.paper.entity.Player(op.getPlayer());
        }
    }

    @Override
    public World getWorld(NamespaceKey key) {
        return new net.kore.meep.paper.world.World(Bukkit.getWorld(new NamespacedKey(key.getNamespace(), key.getPath())));
    }

    @Override
    public List<World> getWorlds() {
        return Bukkit.getWorlds().stream().map(net.kore.meep.paper.world.World::new).collect(Collectors.toUnmodifiableList());
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
    public List<NamespaceKey> getKnownEntityKeys() {
        List<NamespaceKey> entities = new ArrayList<>();
        BuiltInRegistries.ENTITY_TYPE.forEach(e -> {
            ResourceLocation rl = BuiltInRegistries.ENTITY_TYPE.getKey(e);
            entities.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.ENTITY));
        });
        return entities;
    }

    @Override
    public List<NamespaceKey> getKnownParticleKeys() {
        List<NamespaceKey> entities = new ArrayList<>();
        BuiltInRegistries.PARTICLE_TYPE.forEach(p -> {
            ResourceLocation rl = BuiltInRegistries.PARTICLE_TYPE.getKey(p);
            entities.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.PARTICLE));
        });
        return entities;
    }

    @Override
    public List<NamespaceKey> getKnownVillagerTypeKeys() {
        List<NamespaceKey> entities = new ArrayList<>();
        BuiltInRegistries.VILLAGER_TYPE.forEach(t -> {
            ResourceLocation rl = BuiltInRegistries.VILLAGER_TYPE.getKey(t);
            entities.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.VILLAGER_TYPE));
        });
        return entities;
    }

    @Override
    public List<NamespaceKey> getKnownVillagerProfessionKeys() {
        List<NamespaceKey> entities = new ArrayList<>();
        BuiltInRegistries.VILLAGER_PROFESSION.forEach(p -> {
            ResourceLocation rl = BuiltInRegistries.VILLAGER_PROFESSION.getKey(p);
            entities.add(new NamespaceKey(rl.getNamespace(), rl.getPath(), NamespaceKey.KeyType.VILLAGER_PROFESSION));
        });
        return entities;
    }

    @Override
    public WhitelistManager getWhitelistManager() {
        return whitelistManager;
    }

    @Override
    public Console getConsole() {
        return console;
    }

    @Override
    public TaskSchedular getSchedular() {
        return taskSchedular;
    }

    @Override
    public TaskSchedular getAsyncSchedular() {
        return asyncTaskSchedular;
    }

    @Override
    public MeepCommandUtils getCommandUtils() {
        return commandUtils;
    }

    @Override
    public void registerIncomingPackets(String channel, PacketListener packetListener) {
        Bukkit.getMessenger().registerIncomingPluginChannel(MeepPaper.getPlugin(), channel, (s, player, bytes) -> packetListener.onPacket(BukkitToMeep.player(player), s, bytes));
    }

    @Override
    public void registerOutgoingPackets(String channel) {
        Bukkit.getMessenger().registerOutgoingPluginChannel(MeepPaper.getPlugin(), channel);
    }
}
