package net.kore.meep.things.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.command.arguments.ArgumentProvider;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.positioning.Angle;
import net.kore.meep.api.utils.CommandUtils;
import net.kore.meep.api.utils.MathUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class Coordinates {
    public static void register(CommandDispatcher<CommandSender> dispatcher) {
        LiteralCommandNode<CommandSender> node = dispatcher.register(
                ArgumentProvider.literal("coordinates")
                        .requires(sender -> sender.hasPermission("things.command.coordinates"))
                        .executes(ctx -> {
                            ctx.getSource().sendMessage(Component.text("Usage: /coordinates get").color(NamedTextColor.RED));
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(
                                ArgumentProvider.literal("get")
                                        .requires(sender -> sender.hasPermission("things.command.coordinates.get"))
                                        .executes(ctx -> {
                                            net.kore.meep.api.positioning.Coordinates p = CommandUtils.getPosition(ctx.getSource());
                                            if (p != null) {
                                                ctx.getSource().sendMessage(Component.text("Your coordinates are %fx, %fy, %fz".formatted(p.getX(), p.getY(), p.getZ())).color(NamedTextColor.GREEN));
                                            } else {
                                                ctx.getSource().sendMessage(Component.text("This command cannot be run via this source!").color(NamedTextColor.RED));
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(
                                                ArgumentProvider.argument("player", ArgumentProvider.player())
                                                        .requires(sender -> sender.hasPermission("things.command.coordinates.get.player"))
                                                        .executes(ctx -> {
                                                            Player player = ctx.getArgument("player", Player.class);
                                                            net.kore.meep.api.positioning.Coordinates p = player.getPosition().getCoordinates();
                                                            ctx.getSource().sendMessage(
                                                                    Component.text(
                                                                            "%s's coordinates are %fx, %fy, %fz"
                                                                                    .formatted(
                                                                                            PlainTextComponentSerializer.plainText().serialize(
                                                                                                    player.displayName()
                                                                                            ),
                                                                                            p.getX(),
                                                                                            p.getY(),
                                                                                            p.getZ()
                                                                                    )
                                                                    ).color(NamedTextColor.GREEN)
                                                            );
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                        )
                        )
                        .then(
                                ArgumentProvider.literal("distance")
                                        .requires(sender -> sender.hasPermission("things.things.coordinates.distance"))
                                        .executes(ctx -> {
                                            ctx.getSource().sendMessage(Component.text("Usage: /coordinates distance <position|player>").color(NamedTextColor.RED));
                                            return Command.SINGLE_SUCCESS;
                                        })
                                        .then(
                                                ArgumentProvider.argument("other", ArgumentProvider.positionNotPlayer())
                                                        .requires(sender -> sender.hasPermission("things.command.coordinates.distance"))
                                                        .executes(ctx -> {
                                                            net.kore.meep.api.positioning.Coordinates p = CommandUtils.getPosition(ctx.getSource());
                                                            if (p == null) {
                                                                ctx.getSource().sendMessage(Component.text("This command cannot be run via this source!").color(NamedTextColor.RED));
                                                                return Command.SINGLE_SUCCESS;
                                                            }
                                                            double distance;
                                                            net.kore.meep.api.positioning.Coordinates coordinates = ctx.getArgument("other", net.kore.meep.api.positioning.Coordinates.class);
                                                            if (coordinates.isRelative()) {
                                                                if (coordinates.getRelativeType().equals(net.kore.meep.api.positioning.Coordinates.RelativeType.POSITIONAL)) {
                                                                    distance = p.getDistanceFrom(p.clone().add(coordinates));
                                                                } else if (coordinates.getRelativeType().equals(net.kore.meep.api.positioning.Coordinates.RelativeType.DIRECTIONAL)) {
                                                                    Angle angle = CommandUtils.getAngle(ctx.getSource());
                                                                    if (angle == null) distance = p.getDistanceFrom(coordinates);
                                                                    else distance = MathUtils.getTrueDistance(p, angle, coordinates);
                                                                } else {
                                                                    distance = p.getDistanceFrom(coordinates);
                                                                }
                                                            } else {
                                                                distance = p.getDistanceFrom(coordinates);
                                                            }
                                                            ctx.getSource().sendMessage(Component.text("You are %f blocks away from the selected position".formatted(MathUtils.round(distance, 2))));
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                        )
                                        .then(
                                                ArgumentProvider.argument("player", ArgumentProvider.player())
                                                        .requires(sender -> sender.hasPermission("things.command.coordinates.distance.player"))
                                                        .executes(ctx -> {
                                                            net.kore.meep.api.positioning.Coordinates p = CommandUtils.getPosition(ctx.getSource());
                                                            if (p == null) {
                                                                ctx.getSource().sendMessage(Component.text("This command cannot be run via this source!").color(NamedTextColor.RED));
                                                                return Command.SINGLE_SUCCESS;
                                                            }
                                                            Player player = ctx.getArgument("player", Player.class);
                                                            ctx.getSource().sendMessage(
                                                                    Component.text(
                                                                            "You are %f blocks away from %s"
                                                                                    .formatted(
                                                                                            MathUtils.round(
                                                                                                    p.getDistanceFrom(
                                                                                                            player.getPosition().getCoordinates()
                                                                                                    ),
                                                                                                    2
                                                                                            ),
                                                                                            PlainTextComponentSerializer.plainText().serialize(
                                                                                                    player.displayName()
                                                                                            )
                                                                                    )
                                                                    )
                                                            );
                                                            return Command.SINGLE_SUCCESS;
                                                        })
                                        )
                        )
        );
        dispatcher.register(ArgumentProvider.literal("coords").redirect(node));
    }
}
