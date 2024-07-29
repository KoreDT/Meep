package net.kore.meep.things.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import net.kore.meep.api.Meep;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.command.arguments.ArgumentProvider;
import net.kore.meep.api.entity.Entity;
import net.kore.meep.api.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class ServerStats {
    public static void register(CommandDispatcher<CommandSender> dispatcher) {
        dispatcher.register(
                ArgumentProvider.literal("serverstats", "things.command.serverstats")
                        .executes(ctx -> {
                            Runtime r = Runtime.getRuntime();
                            long memUsed = (r.maxMemory() - r.freeMemory()) / 1048576;
                            long memMax = r.maxMemory() / 1048576;

                            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                            DecimalFormat df = new DecimalFormat("#.##");
                            double cpuUsage = Double.parseDouble(df.format(osBean.getCpuLoad() *100));

                            ctx.getSource().sendMessage(
                                    Component.text("Server Statistics")
                                            .color(TextColor.fromCSSHexString("#0e95c2"))
                                            .append(
                                                    Component.text(":")
                                                            .color(NamedTextColor.WHITE)
                                            )
                                            .append(Component.newline())
                                            .append(Component.text("| ").color(NamedTextColor.DARK_GRAY))
                                            .append(
                                                    Component.text("Server TPS")
                                                            .color(TextColor.fromCSSHexString("#18a7d6"))
                                                            .append(
                                                                    Component.text(": "+ Meep.get().getTPS()+"tps")
                                                                            .color(NamedTextColor.WHITE)
                                                            )
                                            )
                                            .append(Component.newline())
                                            .append(Component.text("| ").color(NamedTextColor.DARK_GRAY))
                                            .append(
                                                    Component.text("RAM")
                                                            .color(TextColor.fromCSSHexString("#18a7d6"))
                                                            .append(
                                                                    Component.text(": " + memUsed + "MB/" + memMax + "MB")
                                                                            .color(NamedTextColor.WHITE)
                                                            )
                                            )
                                            .append(Component.newline())
                                            .append(Component.text("| ").color(NamedTextColor.DARK_GRAY))
                                            .append(
                                                    Component.text("CPU")
                                                            .color(TextColor.fromCSSHexString("#18a7d6"))
                                                            .append(
                                                                    Component.text(": " + cpuUsage + "%")
                                                                            .color(NamedTextColor.WHITE)
                                                            )
                                            )
                            );
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(
                                ArgumentProvider.literal("bar", "things.command.serverstats.bar")
                                        .executes(ctx -> {
                                            if (ctx instanceof Player entity) {
                                                Boolean bool = entity.getPersistantBoolean("things_bar");
                                                entity.setPersistantBoolean("things_bar", bool == null || !bool);
                                            } else {
                                                ctx.getSource().sendMessage(Component.text("You cannot run this command from this source.").color(NamedTextColor.RED));
                                            }
                                            return Command.SINGLE_SUCCESS;
                                        })
                        )
        );
    }
}
