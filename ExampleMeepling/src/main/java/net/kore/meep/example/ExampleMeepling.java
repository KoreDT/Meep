package net.kore.meep.example;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.command.arguments.ArgumentProvider;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.event.*;
import net.kore.meep.api.event.command.CommandRegisterEvent;
import net.kore.meep.api.event.lifecycle.DisableEvent;
import net.kore.meep.api.event.lifecycle.EnableEvent;
import net.kore.meep.api.event.lifecycle.ReloadEvent;
import net.kore.meep.api.event.player.PlayerJoinEvent;
import net.kore.meep.api.meepling.Meepling;
import net.kore.meep.api.meepling.Memepling;
import net.kore.meep.api.positioning.Angle;
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.utils.CommandUtils;
import net.kore.meep.api.utils.ExceptionUtils;
import net.kore.meep.api.utils.MathUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.spongepowered.configurate.ConfigurateException;

import java.util.List;

public class ExampleMeepling extends Meepling implements Memepling {
    @Override
    public void init() {
        EventManager.get().registerListener(this);

        EventManager.get().registerListener(CommandRegisterEvent.class, (event) -> {
            LiteralCommandNode<CommandSender> node = event.getCommandDispatcher().register(
                    ArgumentProvider.literal("coordinates")
                            .requires(sender -> sender.hasPermission("meep.command.coordinates"))
                            .executes(ctx -> {
                                ctx.getSource().sendMessage(Component.text("Usage: /coordinates get").color(NamedTextColor.RED));
                                return Command.SINGLE_SUCCESS;
                            })
                            .then(
                                    ArgumentProvider.literal("get")
                                            .requires(sender -> sender.hasPermission("meep.command.coordinates.get"))
                                            .executes(ctx -> {
                                                Coordinates p = CommandUtils.getPosition(ctx.getSource());
                                                if (p != null) {
                                                    ctx.getSource().sendMessage(Component.text("Your coordinates are %fx, %fy, %fz".formatted(p.getX(), p.getY(), p.getZ())).color(NamedTextColor.GREEN));
                                                } else {
                                                    ctx.getSource().sendMessage(Component.text("This command cannot be run via this source!").color(NamedTextColor.RED));
                                                }
                                                return Command.SINGLE_SUCCESS;
                                            })
                                            .then(
                                                    ArgumentProvider.argument("player", ArgumentProvider.player())
                                                            .requires(sender -> sender.hasPermission("meep.command.coordinates.get.player"))
                                                            .executes(ctx -> {
                                                                Player player = ctx.getArgument("player", Player.class);
                                                                Coordinates p = player.getPosition().getCoordinates();
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
                                    ArgumentProvider.argument("other", ArgumentProvider.positionNotPlayer())
                                            .requires(sender -> sender.hasPermission("meep.command.coordinates.distance"))
                                            .executes(ctx -> {
                                                Coordinates p = CommandUtils.getPosition(ctx.getSource());
                                                if (p == null) {
                                                    ctx.getSource().sendMessage(Component.text("This command cannot be run via this source!").color(NamedTextColor.RED));
                                                    return Command.SINGLE_SUCCESS;
                                                }
                                                double distance;
                                                Coordinates coordinates = ctx.getArgument("other", Coordinates.class);
                                                if (coordinates.isRelative()) {
                                                    if (coordinates.getRelativeType().equals(Coordinates.RelativeType.POSITIONAL)) {
                                                        distance = p.getDistanceFrom(p.clone().add(coordinates));
                                                    } else if (coordinates.getRelativeType().equals(Coordinates.RelativeType.DIRECTIONAL)) {
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
                                            .requires(sender -> sender.hasPermission("meep.command.coordinates.distance.player"))
                                            .executes(ctx -> {
                                                Coordinates p = CommandUtils.getPosition(ctx.getSource());
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
            );
            event.getCommandDispatcher().register(ArgumentProvider.literal("coords").redirect(node));
        });
    }

    @Override
    public boolean shouldUseDefaultConfig() {
        return true;
    }

    // A meme API, yes I did it.
    @Override
    public List<Component> memeps() {
        return List.of(Component.text("When you tap someone on the shoulder on the left but you're at the right:").color(NamedTextColor.GOLD));
    }

    @EventListener(event = EnableEvent.class)
    public void onEnable() {
        getLogger().info(getConfig().node("meepling-start-message").getString("Enabling meepling..."));
    }

    @EventListener(event = DisableEvent.class)
    public void onDisable() {
        getLogger().info(getConfig().node("meepling-stop-message").getString("Disabling meepling..."));
    }

    @EventListener()
    public void onReload(ReloadEvent e) {
        try {
            handleDefaultReloadElseThrow();
            e.showValidReload(this);
        } catch (ConfigurateException ex) {
            e.presentIssue(this, "Unable to reload config, please check console!");
            getLogger().info(ExceptionUtils.throwableToString(ex));
        }
    }

    @EventListener
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage(Component.text("Welcome!").color(NamedTextColor.GOLD));
    }
}
