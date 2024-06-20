/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.RedirectModifier;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kore.meep.api.Console;
import net.kore.meep.api.Meep;
import net.kore.meep.api.command.CommandBlockSender;
import net.kore.meep.api.command.CommandProxySender;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.event.EventManager;
import net.kore.meep.api.event.command.CommandRegisterEvent;
import net.kore.meep.api.event.lifecycle.DisableEvent;
import net.kore.meep.api.event.lifecycle.EnableEvent;
import net.kore.meep.api.event.lifecycle.StartEvent;
import net.kore.meep.api.permission.PermissionManager;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.paper.event.PaperEventListener;
import net.kore.meep.paper.permission.PaperPermissionManager;
import net.kore.meep.paper.task.PaperAsyncTaskSchedular;
import net.kore.meep.paper.task.PaperTaskSchedular;
import net.kore.meep.paper.utils.BukkitToMeep;
import net.kyori.adventure.text.Component;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.commands.TeleportCommand;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("UnstableApiUsage")
public final class MeepPaper extends JavaPlugin implements Listener {
    private CommandDispatcher<CommandSender> DISPATCHER;

    @Override
    public void onLoad() {
        getLogger().info("Starting Meep...");
        PermissionManager.set(new PaperPermissionManager());
        Meep.set(new PaperyMeep());
        Meep.init(getDataFolder().getParentFile().getParentFile(), getLogger());
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            StartEvent event = new StartEvent();
            EventManager.get().fireEvent(event);
        });

        EnableEvent event = new EnableEvent("Paper");
        EventManager.get().fireEvent(event);

        Bukkit.getPluginManager().registerEvents(new PaperEventListener(), this);

        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, e -> {
            Commands commands = e.registrar();

            CommandRegisterEvent commandRegisterEvent = new CommandRegisterEvent();
            EventManager.get().fireEvent(commandRegisterEvent);

            DISPATCHER = commandRegisterEvent.getCommandDispatcher();

            for (CommandNode<CommandSender> command : DISPATCHER.getRoot().getChildren()) {
                if (command instanceof LiteralCommandNode<CommandSender> literalCommand) {
                    commands.register(
                            ((LiteralCommandNode<CommandSourceStack>) translateNode(literalCommand)),
                            null,
                            Collections.emptyList()
                    );
                }
            }
        });

        Bukkit.getGlobalRegionScheduler().runAtFixedRate(this, (task) -> ((PaperTaskSchedular) Meep.get().getSchedular()).onTick(), 1, 1);
        Bukkit.getAsyncScheduler().runAtFixedRate(this, (task) -> ((PaperAsyncTaskSchedular) Meep.get().getAsyncSchedular()).onTick(), 50, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDisable() {
        DisableEvent event = new DisableEvent();
        EventManager.get().fireEvent(event);
    }

    public CommandNode<CommandSourceStack> translateNode(CommandNode<CommandSender> original) {
        if (original instanceof LiteralCommandNode<CommandSender> literalCommandNode) {
            LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(literalCommandNode.getLiteral()).executes(commandContext -> {
                CommandSender sender = getSender(commandContext.getSource());
                if (sender == null) {
                    throw new CommandSyntaxException(new CommandExceptionType() {}, () -> "Unsupported CommandSender type for Meep. Report this to the GitHub. Missing CommandSender type: "+commandContext.getSource().getSender().getClass().getSimpleName());
                }
                return DISPATCHER.execute(commandContext.getInput(), sender);
            });
            for (CommandNode<CommandSender> command : literalCommandNode.getChildren()) {
                literal = literal.then(translateNode(command));
            }
            CommandNode<CommandSender> redirect = literalCommandNode.getRedirect();
            boolean isForking = literalCommandNode.isFork();
            if (redirect != null) {
                literal = literal.forward(translateNode(redirect), null, isForking); // Modifier can be null because we once again handle this on the other dispatcher
            }
            return literal.build();
        } else if (original instanceof ArgumentCommandNode<CommandSender ,?> argumentCommandNode) {
            ArgumentType<?> argType;
            if (argumentCommandNode.getType() instanceof IntegerArgumentType integerArgumentType) {
                argType = integerArgumentType;
            } else if (argumentCommandNode.getType() instanceof BoolArgumentType boolArgumentType) {
                argType = boolArgumentType;
            } else if (argumentCommandNode.getType() instanceof DoubleArgumentType doubleArgumentType) {
                argType = doubleArgumentType;
            } else if (argumentCommandNode.getType() instanceof FloatArgumentType floatArgumentType) {
                argType = floatArgumentType;
            } else if (argumentCommandNode.getType() instanceof LongArgumentType longArgumentType) {
                argType = longArgumentType;
            } else if (argumentCommandNode.getType() instanceof StringArgumentType stringArgumentType) {
                argType = stringArgumentType;
            } else {
                argType = new CustomArgumentType<Object, String>() {
                    @Override
                    public @NotNull Object parse(@NotNull StringReader stringReader) {
                        return new Object(); // This doesn't matter because we parse the command straight to Meep's dispatcher which will handle the parsing much better than paper could ever
                    }

                    @Override
                    public @NotNull ArgumentType<String> getNativeType() {
                        return StringArgumentType.word();
                    }

                    @Override
                    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
                        if (context.getSource() instanceof CommandSourceStack stack) {
                            try {
                                Map<String, ParsedArgument<CommandSender, ?>> MEEP_ARGS = new HashMap<>();
                                @SuppressWarnings("unchecked")
                                Map<String, ParsedArgument<CommandSourceStack, ?>> ARGS = (Map<String, ParsedArgument<CommandSourceStack, ?>>) context.getClass().getDeclaredField("arguments").get(context);
                                ARGS.forEach((str, parsedArg) -> {
                                    ParsedArgument<CommandSender, ?> pa = new ParsedArgument<>(parsedArg.getRange().getStart(), parsedArg.getRange().getEnd(), parsedArg.getResult());
                                    MEEP_ARGS.put(str, pa);
                                });
                                return argumentCommandNode.listSuggestions(new CommandContext<>(
                                        getSender(stack),
                                        context.getInput(),
                                        MEEP_ARGS,
                                        (ctx) -> context.getCommand().run(context),
                                        null,
                                        null,
                                        context.getRange(),
                                        null,
                                        null,
                                        context.isForked()
                                ), builder);
                            } catch (Exception ignored) {}
                        }
                        return Suggestions.empty();
                    }
                };
            }
            RequiredArgumentBuilder<CommandSourceStack, ?> argument = Commands.argument(argumentCommandNode.getName(), argType);
            argument.executes(commandContext -> {
                CommandSender sender = getSender(commandContext.getSource());
                if (sender == null) {
                    throw new CommandSyntaxException(new CommandExceptionType() {}, () -> "Unsupported CommandSender type for Meep.");
                }
                return DISPATCHER.execute(commandContext.getInput(), sender);
            });
            for (CommandNode<CommandSender> command : argumentCommandNode.getChildren()) {
                argument = argument.then(translateNode(command));
            }
            CommandNode<CommandSender> redirect = argumentCommandNode.getRedirect();
            boolean isForking = argumentCommandNode.isFork();
            if (redirect != null) {
                argument = argument.forward(translateNode(redirect), null, isForking); // Modifier can be null because we once again handle this on the other dispatcher
            }
            return argument.build();
        } else if (original instanceof RootCommandNode<CommandSender>) {
            throw new RuntimeException("Cannot translate RootCommandNode.");
        } else {
            throw new RuntimeException("Unknown CommandNode.");
        }
    }

    public CommandSender getSender(CommandSourceStack stack) {
        return getSender(stack.getSender());
    }

    public CommandSender getSender(org.bukkit.command.CommandSender sender) {
        if (sender instanceof Entity player) {
            return new net.kore.meep.paper.entity.Entity(player);
        } else if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
            return Meep.get().getConsole();
        } else if (sender instanceof BlockCommandSender block) {
            Location l = block.getBlock().getLocation();
            return new CommandBlockSender(BukkitToMeep.world(block.getBlock().getWorld()).getBlockAt(new Coordinates(l.x(), l.y(), l.z())));
        } else if (sender instanceof ProxiedCommandSender s) {
            return new CommandProxySender(getSender(s.getCaller()), getSender(s.getCallee()));
        }
        return null;
    }

    public org.bukkit.command.CommandSender getSender(CommandSender sender) {
        if (sender instanceof net.kore.meep.paper.entity.Entity e) {
            return e.getParent();
        } else if (sender instanceof CommandBlockSender block) {
            World w = ((net.kore.meep.paper.world.World)block.getBlock().getWorldPosition().getWorld()).getParent();
            Coordinates coords = block.getBlock().getWorldPosition().getCoordinates();
            Block bl = w.getBlockAt(new Location(w, coords.getX(), coords.getY(), coords.getZ()));
            return new BlockCommandSender() {
                @Override
                public @NotNull Block getBlock() {
                    return bl;
                }

                @Override
                public void sendMessage(@NotNull String s) {

                }

                @Override
                public void sendMessage(@NotNull String... strings) {

                }

                @Override
                public void sendMessage(@Nullable UUID uuid, @NotNull String s) {

                }

                @Override
                public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {

                }

                @Override
                public @NotNull Server getServer() {
                    return Bukkit.getServer();
                }

                @Override
                public @NotNull String getName() {
                    return "";
                }

                @NotNull
                @Override
                public Spigot spigot() {
                    return new Spigot();
                }

                @Override
                public @NotNull Component name() {
                    return Component.text("Unknown Name.");
                }

                @Override
                public boolean isPermissionSet(@NotNull String s) {
                    return true;
                }

                @Override
                public boolean isPermissionSet(@NotNull Permission permission) {
                    return true;
                }

                @Override
                public boolean hasPermission(@NotNull String s) {
                    return true;
                }

                @Override
                public boolean hasPermission(@NotNull Permission permission) {
                    return true;
                }

                @Override
                public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
                    return new PermissionAttachment(plugin, this);
                }

                @Override
                public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
                    return new PermissionAttachment(plugin, this);
                }

                @Override
                public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
                    return new PermissionAttachment(plugin, this);
                }

                @Override
                public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
                    return new PermissionAttachment(plugin, this);
                }

                @Override
                public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {

                }

                @Override
                public void recalculatePermissions() {

                }

                @Override
                public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
                    return new HashSet<>();
                }

                @Override
                public boolean isOp() {
                    return true;
                }

                @Override
                public void setOp(boolean b) {

                }
            };
        } else if (sender instanceof CommandProxySender proxy) {
            return new ProxiedCommandSender() {
                @Override
                public org.bukkit.command.@NotNull CommandSender getCaller() {
                    return getSender(proxy.caller());
                }

                @Override
                public org.bukkit.command.@NotNull CommandSender getCallee() {
                    return getSender(proxy.callee());
                }

                @Override
                public void sendMessage(@NotNull String s) {

                }

                @Override
                public void sendMessage(@NotNull String... strings) {

                }

                @Override
                public void sendMessage(@Nullable UUID uuid, @NotNull String s) {

                }

                @Override
                public void sendMessage(@Nullable UUID uuid, @NotNull String... strings) {

                }

                @Override
                public @NotNull Server getServer() {
                    return Bukkit.getServer();
                }

                @Override
                public @NotNull String getName() {
                    return "";
                }

                @NotNull
                @Override
                public Spigot spigot() {
                    return new Spigot();
                }

                @Override
                public @NotNull Component name() {
                    return Component.text("Unknown Name.");
                }

                @Override
                public boolean isPermissionSet(@NotNull String s) {
                    return hasPermission(s);
                }

                @Override
                public boolean isPermissionSet(@NotNull Permission permission) {
                    return hasPermission(permission);
                }

                @Override
                public boolean hasPermission(@NotNull String s) {
                    return getCallee().hasPermission(s);
                }

                @Override
                public boolean hasPermission(@NotNull Permission permission) {
                    return getCallee().hasPermission(permission);
                }

                @Override
                public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b) {
                    return new PermissionAttachment(plugin, this);
                }

                @Override
                public @NotNull PermissionAttachment addAttachment(@NotNull Plugin plugin) {
                    return new PermissionAttachment(plugin, this);
                }

                @Override
                public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, @NotNull String s, boolean b, int i) {
                    return null;
                }

                @Override
                public @Nullable PermissionAttachment addAttachment(@NotNull Plugin plugin, int i) {
                    return null;
                }

                @Override
                public void removeAttachment(@NotNull PermissionAttachment permissionAttachment) {

                }

                @Override
                public void recalculatePermissions() {

                }

                @Override
                public @NotNull Set<PermissionAttachmentInfo> getEffectivePermissions() {
                    return Set.of();
                }

                @Override
                public boolean isOp() {
                    return getCallee().isOp();
                }

                @Override
                public void setOp(boolean b) {
                    getCallee().setOp(b);
                }
            };
        } else if (sender instanceof Console console) {
            return Bukkit.getConsoleSender();
        } else {
            return null;
        }
    }
}
