package net.kore.meep.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kore.meep.api.Meep;
import net.kore.meep.api.command.CommandBlockSender;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.event.EventManager;
import net.kore.meep.api.event.command.CommandRegisterEvent;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.fabric.world.World;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class MeepFabric implements DedicatedServerModInitializer {
    public static final String MOD_ID = "meepfabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static DedicatedServer server;
    public static DedicatedServer getServer() {
        return server;
    }
    CommandDispatcher<CommandSender> DISPATCHER;

    @Override
    public void onInitializeServer() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (MeepFabric.server != null && server instanceof DedicatedServer ds) {
                MeepFabric.server = ds;
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandRegisterEvent event = new CommandRegisterEvent();
            EventManager.get().fireEvent(event);

            DISPATCHER = event.getCommandDispatcher();

            for (CommandNode<CommandSender> command : DISPATCHER.getRoot().getChildren()) {
                if (command instanceof LiteralCommandNode<CommandSender> literalCommand) {
                    dispatcher.register(
                            ((LiteralCommandNode<CommandSourceStack>) translateNode(literalCommand, DISPATCHER.getRoot())).createBuilder()
                    );
                }
            }
        });

        Meep.set(new FabricyMeep());
        Meep.init(FabricLoader.getInstance().getConfigDir().toFile().getParentFile().getParentFile(), LOGGER);
    }

    public CommandNode<CommandSourceStack> translateNode(CommandNode<CommandSender> original, RootCommandNode<CommandSender> root) {
        switch (original) {
            case LiteralCommandNode<CommandSender> literalCommandNode -> {
                LiteralArgumentBuilder<CommandSourceStack> literal = literal(literalCommandNode.getLiteral()).executes(commandContext -> {
                    CommandSender sender = getSender(commandContext.getSource());
                    if (sender == null) {
                        throw new CommandSyntaxException(new CommandExceptionType() {
                        }, () -> "Unsupported CommandSender type for Meep. Report this to the GitHub. Missing CommandSender type: " + commandContext.getSource().getClass().getSimpleName());
                    }
                    return DISPATCHER.execute(commandContext.getInput(), sender);
                });
                for (CommandNode<CommandSender> command : literalCommandNode.getChildren()) {
                    literal = literal.then(translateNode(command, root));
                }
                CommandNode<CommandSender> redirect = literalCommandNode.getRedirect();
                boolean isForking = literalCommandNode.isFork();
                if (redirect != null) {
                    literal = literal.forward(translateNode(redirect, root), null, isForking); // Modifier can be null because we once again handle this on the other dispatcher
                }
                return literal.build();
            }
            case ArgumentCommandNode<CommandSender, ?> argumentCommandNode -> {
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
                    argType = StringArgumentType.word();
                }
                RequiredArgumentBuilder<CommandSourceStack, ?> argument = argument(argumentCommandNode.getName(), argType);
                argument.executes(commandContext -> {
                    CommandSender sender = getSender(commandContext.getSource());
                    if (sender == null) {
                        throw new CommandSyntaxException(new CommandExceptionType() {
                        }, () -> "Unsupported CommandSender type for Meep.");
                    }
                    return DISPATCHER.execute(commandContext.getInput(), sender);
                });
                argument.suggests((context, builder) -> {
                    try {
                        Map<String, ParsedArgument<CommandSender, ?>> MEEP_ARGS = new HashMap<>();
                        @SuppressWarnings("unchecked")
                        Map<String, ParsedArgument<CommandSourceStack, ?>> ARGS = (Map<String, ParsedArgument<CommandSourceStack, ?>>) context.getClass().getDeclaredField("arguments").get(context);
                        ARGS.forEach((str, parsedArg) -> {
                            ParsedArgument<CommandSender, ?> pa = new ParsedArgument<>(parsedArg.getRange().getStart(), parsedArg.getRange().getEnd(), parsedArg.getResult());
                            MEEP_ARGS.put(str, pa);
                        });
                        return argumentCommandNode.listSuggestions(new CommandContext<>(
                                getSender(context.getSource()),
                                context.getInput(),
                                MEEP_ARGS,
                                (ctx) -> context.getCommand().run(context),
                                root,
                                null,
                                context.getRange(),
                                null,
                                null,
                                context.isForked()
                        ), builder);
                    } catch (Exception ignored) {
                    }
                    return Suggestions.empty();
                });
                for (CommandNode<CommandSender> command : argumentCommandNode.getChildren()) {
                    argument = argument.then(translateNode(command, DISPATCHER.getRoot()));
                }
                CommandNode<CommandSender> redirect = argumentCommandNode.getRedirect();
                boolean isForking = argumentCommandNode.isFork();
                if (redirect != null) {
                    argument = argument.forward(translateNode(redirect, DISPATCHER.getRoot()), null, isForking); // Modifier can be null because we once again handle this on the other dispatcher
                }
                return argument.build();
            }
            case RootCommandNode<CommandSender> ignored ->
                    throw new RuntimeException("Cannot translate RootCommandNode.");
            case null, default -> throw new RuntimeException("Unknown CommandNode.");
        }
    }

    public CommandSender getSender(CommandSourceStack sender) {
        Vec3 blockLocation = null;
        ServerLevel level = null;
        if (sender.source instanceof Entity e) {
            return new net.kore.meep.fabric.entity.Entity(e);
        } else if (sender.source instanceof MinecraftServer || sender.source instanceof RconConsoleSource) {
            return Meep.get().getConsole();
        } else if (sender.source instanceof BaseCommandBlock block) {
            blockLocation = block.getPosition();
            level = block.getLevel();
        }

        if (blockLocation != null) {
            return new CommandBlockSender(new World(level).getBlockAt(new Coordinates(blockLocation.x(), blockLocation.y(), blockLocation.z())));
        }

        return null;
    }

    private LiteralArgumentBuilder<CommandSourceStack> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    private <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String node, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(node, type);
    }
}
