/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api;

import com.mojang.brigadier.Command;
import net.kore.meep.api.command.arguments.ArgumentProvider;
import net.kore.meep.api.event.EventManager;
import net.kore.meep.api.event.block.BlockBreakEvent;
import net.kore.meep.api.event.block.BlockPlaceEvent;
import net.kore.meep.api.event.command.CommandRegisterEvent;
import net.kore.meep.api.event.entity.EntityDamageEvent;
import net.kore.meep.api.event.entity.EntityDeathEvent;
import net.kore.meep.api.event.lifecycle.DisableEvent;
import net.kore.meep.api.event.lifecycle.EnableEvent;
import net.kore.meep.api.event.lifecycle.ReloadEvent;
import net.kore.meep.api.event.lifecycle.StartEvent;
import net.kore.meep.api.event.player.PlayerChatEvent;
import net.kore.meep.api.event.player.PlayerJoinEvent;
import net.kore.meep.api.event.player.PlayerLeaveEvent;
import net.kore.meep.api.event.player.PlayerSwitchWorldEvent;
import net.kore.meep.api.event.server.WhitelistModifyEvent;
import net.kore.meep.api.event.world.WorldLoadEvent;
import net.kore.meep.api.event.world.WorldUnloadEvent;
import net.kore.meep.api.logger.Logger;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.meepling.Meepling;
import net.kore.meep.api.permission.Permission;
import net.kore.meep.api.permission.PermissionManager;
import net.kore.meep.api.meepling.loader.JSLoader;
import net.kore.meep.api.meepling.loader.JavaLoader;
import net.kore.meep.api.task.TaskSchedular;
import net.kore.meep.api.world.World;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("unused")
public abstract class Meep {
    public static Object logger;
    private static Logger masterLogger;
    public static Logger getMasterLogger() {
        return masterLogger;
    }

    private static File serverDirectory;
    public static File getServerDirectory() {
        return serverDirectory;
    }


    private static Meep INSTANCE;

    @ApiStatus.Internal
    public static void set(Meep meep) { // Treat as an init function for certain static setups
        INSTANCE = meep;
    }

    public static Meep get() {
        return INSTANCE;
    }

    public static final List<Component> memeps = new ArrayList<>();

    @ApiStatus.Internal
    public static void init(File serverDirectory, Object logger) {
        Meep.logger = logger;
        masterLogger = new Logger("Meep");
        Meep.serverDirectory = serverDirectory;

        EventManager.get().registerEvents(
                List.of(
                        //Entity events
                        EntityDamageEvent.class,
                        EntityDeathEvent.class,

                        //Player events
                        PlayerChatEvent.class,
                        PlayerJoinEvent.class,
                        PlayerLeaveEvent.class,
                        PlayerSwitchWorldEvent.class,

                        //Block events
                        BlockBreakEvent.class,
                        BlockPlaceEvent.class,

                        //Lifecycle events
                        EnableEvent.class,
                        DisableEvent.class,
                        ReloadEvent.class,
                        StartEvent.class,

                        //Command events
                        CommandRegisterEvent.class,

                        //Server events
                        WhitelistModifyEvent.class,

                        //World events
                        WorldLoadEvent.class,
                        WorldUnloadEvent.class
                )
        );

        EventManager.get().registerListener(EnableEvent.class, (e) -> {
            Permission meepCommand = new Permission("meep.command.meep", "Grants access to the Meep command", Permission.Default.OP);
            Permission meepHelpCommand = new Permission("meep.command.meep.help", "Grants access to the help Meep command", Permission.Default.OP);
            Permission meepReloadCommand = new Permission("meep.command.meep.reload", "Grants access to the reload Meep command", Permission.Default.OP);
            Permission memepCommand = new Permission("meep.command.memep", "Grants access to the Memep command", Permission.Default.OP);
            PermissionManager.get().registerPermissions(meepCommand, meepHelpCommand, meepReloadCommand, memepCommand);
        });

        EventManager.get().registerListener(CommandRegisterEvent.class, (e) -> {
            e.getCommandDispatcher().register(
                    ArgumentProvider.literal("meep")
                            .requires((sender) -> sender.hasPermission("meep.command.meep"))
                            .executes(ctx -> {
                                ctx.getSource().sendMessage(Component.text("Usage: /meep <help|reload>").color(NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            })
                            .then(
                                    ArgumentProvider.literal("help")
                                            .requires((sender) -> sender.hasPermission("meep.command.meep.help"))
                                            .executes(ctx -> {
                                                ctx.getSource().sendMessage(Component.text("Meep commands:\n/meep help | Shows the help command info\n/meep reload | Reloads all plugin configs (if a plugin supports it)").color(NamedTextColor.GOLD));
                                                return Command.SINGLE_SUCCESS;
                                            })
                                            .then(
                                                    ArgumentProvider.literal("help")
                                                            .executes(ctx -> {
                                                                ctx.getSource().sendMessage(Component.text("/meep help | Shows the help command info").color(NamedTextColor.GOLD));
                                                                return Command.SINGLE_SUCCESS;
                                                            })
                                            )
                                            .then(
                                                    ArgumentProvider.literal("reload")
                                                            .executes(ctx -> {
                                                                ctx.getSource().sendMessage(Component.text("/meep reload | Reloads all plugin configs (if a plugin supports it)").color(NamedTextColor.GOLD));
                                                                return Command.SINGLE_SUCCESS;
                                                            })
                                            )
                            )
                            .then(
                                    ArgumentProvider.literal("reload")
                                            .requires((sender) -> sender.hasPermission("meep.command.meep.reload"))
                                            .executes(ctx -> {
                                                ctx.getSource().sendMessage(Component.text("Reloading now...").color(NamedTextColor.YELLOW));
                                                EventManager.get().fireEvent(new ReloadEvent(ctx.getSource()));
                                                return Command.SINGLE_SUCCESS;
                                            })
                            )
                            .then(
                                    ArgumentProvider.literal("meepling")
                                            .requires((sender) -> sender.hasPermission("meep.command.meep.meepling"))
                                            .executes(ctx -> {
                                                ctx.getSource().sendMessage(Component.text("Usage: /meep meepling <meepling_name>").color(NamedTextColor.RED));
                                                return Command.SINGLE_SUCCESS;
                                            })
                                            .then(
                                                    ArgumentProvider.argument("meepling", ArgumentProvider.meepling())
                                                            .executes(ctx -> {
                                                                Meepling meepling = ctx.getArgument("meepling", Meepling.class);
                                                                ctx.getSource().sendMessage(MiniMessage.miniMessage().deserialize(
                                                                        "<green>%s</green>:\n- <yellow>Version</yellow>: %s\n- <yellow>Description</yellow>: %s"
                                                                                .formatted(meepling.getName(), meepling.getVersion(), meepling.getDescription().isEmpty() ? "No description provided." : meepling.getDescription())
                                                                ));
                                                                return Command.SINGLE_SUCCESS;
                                                            })
                                            )
                            )
            );
            if (get().getConfig().node("memeps.enabled").getBoolean(true)) {
                e.getCommandDispatcher().register(
                        ArgumentProvider.literal("memep")
                                .requires((sender) -> sender.hasPermission("meep.command.memep"))
                                .executes(ctx -> {
                                    if (memeps.isEmpty()) {
                                        ctx.getSource().sendMessage(Component.text("There are no memeps provided :(").color(NamedTextColor.RED));
                                    } else {
                                        Random r = new Random();
                                        Component memep = memeps.get(r.nextInt(memeps.size()));
                                        ctx.getSource().sendMessage(memep);
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                );
            }
        });

        File configFile = new File(serverDirectory, "meep.conf");
        if (!configFile.exists() || !configFile.isFile()) {
            if (!configFile.isFile()) {
                get().executeCrash(null, "Meep config file is not a file.");
            }
            try {
                InputStream is = Meep.class.getResourceAsStream("meep.conf");
                if (is == null) throw new FileNotFoundException("Default 'meep.conf' not found!");
                OutputStream os = new FileOutputStream(configFile);
                os.write(is.readAllBytes());
                os.close();
            } catch (Exception e) {
                get().executeCrash(e, "Could not write default config for Meep");
            }
        }

        //Config loading...

        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .file(configFile)
                .build();

        try {
            get().setConfig(loader.load());
        } catch (ConfigurateException e) {
            get().executeCrash(e, "Could not read config file for Meep");
        }

        //Now, to load our stuffs

        JavaLoader javaLoader = new JavaLoader();
        JSLoader jsLoader = new JSLoader();
        File meeplingDirectory = new File(serverDirectory, "meeplings");
        File[] files = meeplingDirectory.listFiles();
        if (files != null) {
            for (File meepling : files) {
                if (meepling.isDirectory()) {
                    jsLoader.loadFile(meepling);
                } else if (meepling.isFile()) {
                    javaLoader.loadFile(meepling);
                } else {
                    throw new IllegalStateException("Meepling is not a directory or file. How?");
                }
            }
        }
    }

    private CommentedConfigurationNode config;

    private void setConfig(CommentedConfigurationNode node) {
        this.config = node;
    }

    @ApiStatus.Internal
    public CommentedConfigurationNode getConfig() {
        return config;
    }

    @ApiStatus.Internal
    public abstract void executeCrash(@Nullable Throwable t, String message);

    /**
     * Get all online players
     * @return {@link List}<{@link Player}>
     */
    public abstract List<Player> getOnlinePlayers();

    /**
     * Get player via UUID
     * @param uuid The UUID of the player
     * @return {@link Player}
     */
    public abstract Player getPlayer(UUID uuid);

    /**
     * Get the world via NamespaceKey
     * @param key The key of the world
     * @return {@link NamespaceKey}
     */
    public abstract World getWorld(NamespaceKey key);

    /**
     * Get all worlds loaded
     * @return {@link World}[]
     */
    public abstract List<World> getWorlds();

    /**
     * Get all known block keys
     * @return {@link List}<{@link NamespaceKey}>
     */
    public abstract List<NamespaceKey> getKnownBlockKeys();

    /**
     * Get all known enchant keys
     * @return {@link List}<{@link NamespaceKey}>
     */
    public abstract List<NamespaceKey> getKnownEnchantKeys();

    /**
     * Get all known entity keys
     * @return {@link List}<{@link NamespaceKey}>
     */
    public abstract List<NamespaceKey> getKnownEntityKeys();

    /**
     * Get all known item keys
     * @return {@link List}<{@link NamespaceKey}>
     */
    public abstract List<NamespaceKey> getKnownItemKeys();

    /**
     * Get all known particle keys
     * @return {@link List}<{@link NamespaceKey}>
     */
    public abstract List<NamespaceKey> getKnownParticleKeys();

    /**
     * Get all known villager type keys
     * @return {@link List}<{@link NamespaceKey}>
     */
    public abstract List<NamespaceKey> getKnownVillagerTypeKeys();

    /**
     * Get all known villager profession keys
     * @return {@link List}<{@link NamespaceKey}>
     */
    public abstract List<NamespaceKey> getKnownVillagerProfessionKeys();

    /**
     * Get the whitelist manager
     * @return {@link WhitelistManager}
     */
    public abstract WhitelistManager getWhitelistManager();

    /**
     * Get the console
     * @return {@link Console}
     */
    public abstract Console getConsole();

    /**
     * Get the task schedular
     * @return {@link TaskSchedular}
     */
    public abstract TaskSchedular getSchedular();

    /**
     * Get the async task schedular
     * @return {@link TaskSchedular}
     */
    public abstract TaskSchedular getAsyncSchedular();
}
