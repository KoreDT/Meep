package net.kore.meep.fabric;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.Suggestions;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.kore.meep.api.Meep;
import net.kore.meep.api.command.Command;
import net.kore.meep.api.command.CommandBlockSender;
import net.kore.meep.api.command.CommandException;
import net.kore.meep.api.event.EventManager;
import net.kore.meep.api.event.command.CommandRegisterEvent;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.fabric.entity.Player;
import net.kore.meep.fabric.command.FabricCommandManager;
import net.kore.meep.fabric.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.BaseCommandBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class MeepFabric implements DedicatedServerModInitializer {
    public static final String MOD_ID = "meepfabric";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static DedicatedServer server;
    public static DedicatedServer getServer() {
        return server;
    }

    @Override
    public void onInitializeServer() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (MeepFabric.server != null && server instanceof DedicatedServer ds) {
                MeepFabric.server = ds;
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            CommandRegisterEvent event = new CommandRegisterEvent(new FabricCommandManager());
            EventManager.get().fireEvent(event);
            List<Command> commands = ((FabricCommandManager) event.getCommandManager()).commands;

            for (Command command : commands) {
                dispatcher.register(literal(command.getName())
                        .executes(ctx -> {
                            try {
                                if (ctx.getSource().isPlayer()) {
                                    command.execute(new Player(ctx.getSource().getPlayer()), new ArrayList<>());
                                } else if (ctx.getSource().source instanceof MinecraftServer) {
                                    command.execute(Meep.get().getConsole(), new ArrayList<>());
                                } else if (ctx.getSource().source instanceof BaseCommandBlock block) {
                                    command.execute(new CommandBlockSender(new World(block.getLevel()).getBlockAt(new Coordinates(block.getPosition().x, block.getPosition().y, block.getPosition().z))), new ArrayList<>());
                                }
                            } catch (CommandException e) {
                                throw new RuntimeException(e);
                            }

                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                        })
                        .then(
                                argument("args", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            try {
                                                if (ctx.getSource().isPlayer()) {
                                                    command.execute(new Player(ctx.getSource().getPlayer()), List.of(StringArgumentType.getString(ctx, "args").split(" ")));
                                                } else if (ctx.getSource().source instanceof MinecraftServer) {
                                                    command.execute(Meep.get().getConsole(), List.of(StringArgumentType.getString(ctx, "args").split(" ")));
                                                } else if (ctx.getSource().source instanceof BaseCommandBlock block) {
                                                    command.execute(new CommandBlockSender(new World(block.getLevel()).getBlockAt(new Coordinates(block.getPosition().x, block.getPosition().y, block.getPosition().z))), List.of(StringArgumentType.getString(ctx, "args").split(" ")));
                                                }
                                            } catch (CommandException e) {
                                                throw new RuntimeException(e);
                                            }

                                            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                                        })
                                        .suggests(
                                                (ctx, suggestionsBuilder) -> {
                                                    List<String> strings = List.of(StringArgumentType.getString(ctx, "args").split(" "));
                                                    List<String> suggestions = command.suggest(strings.indexOf(strings.getLast()), strings.getLast(), strings);
                                                    if (suggestions != null) {
                                                        suggestions.forEach(suggestionsBuilder::suggest);
                                                        return suggestionsBuilder.buildFuture();
                                                    } else {
                                                        return Suggestions.empty();
                                                    }
                                                }
                                        )
                        )
                );
            }
        });

        Meep.set(new FabricyMeep());
        Meep.init(FabricLoader.getInstance().getConfigDir().toFile().getParentFile().getParentFile(), LOGGER);
    }
}
