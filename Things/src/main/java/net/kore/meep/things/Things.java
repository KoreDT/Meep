package net.kore.meep.things;

import com.sun.management.OperatingSystemMXBean;
import net.kore.meep.api.Meep;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.event.*;
import net.kore.meep.api.event.command.CommandRegisterEvent;
import net.kore.meep.api.event.lifecycle.ReloadEvent;
import net.kore.meep.api.meepling.Meepling;
import net.kore.meep.api.meepling.Memepling;
import net.kore.meep.api.task.MeepTask;
import net.kore.meep.api.utils.ExceptionUtils;
import net.kore.meep.things.commands.Coordinates;
import net.kore.meep.things.commands.ServerStats;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.spongepowered.configurate.ConfigurateException;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.util.List;

public class Things extends Meepling implements Memepling {
    @Override
    public void init() {
        EventManager.get().registerListener(this);

        EventManager.get().registerListener(CommandRegisterEvent.class, (event) -> {
            Coordinates.register(event.getCommandDispatcher());
            ServerStats.register(event.getCommandDispatcher());
        });

        Meep.get().getSchedular().registerTask(new MeepTask() {
            @Override
            public int delay() {
                return 0;
            }

            @Override
            public Integer repeatAfter() {
                return 20;
            }

            @Override
            public void run() {
                for (Player player : Meep.get().getOnlinePlayers()) {
                    if (Boolean.TRUE.equals(player.getPersistantBoolean("things_bar"))) {
                        Runtime r = Runtime.getRuntime();
                        long memUsed = (r.maxMemory() - r.freeMemory()) / 1048576;
                        long memMax = r.maxMemory() / 1048576;

                        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
                        DecimalFormat df = new DecimalFormat("#.##");
                        double cpuUsage = Double.parseDouble(df.format(osBean.getProcessCpuLoad() * 100));

                        Component component = Component.text("Server TPS")
                                .color(TextColor.fromCSSHexString("#18a7d6"))
                                .append(
                                        Component.text(": " + Meep.get().getTPS() + "tps")
                                                .color(NamedTextColor.WHITE)
                                )
                                .append(Component.text(" | ").color(NamedTextColor.DARK_GRAY))
                                .append(
                                        Component.text("RAM")
                                                .color(TextColor.fromCSSHexString("#18a7d6"))
                                                .append(
                                                        Component.text(": " + memUsed + "MB/" + memMax + "MB")
                                                                .color(NamedTextColor.WHITE)
                                                )
                                )
                                .append(Component.text(" | ").color(NamedTextColor.DARK_GRAY))
                                .append(
                                        Component.text("CPU")
                                                .color(TextColor.fromCSSHexString("#18a7d6"))
                                                .append(
                                                        Component.text(": " + cpuUsage + "%")
                                                                .color(NamedTextColor.WHITE)
                                                )
                                );
                        player.showBossBar(BossBar.bossBar(component, (float) cpuUsage, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS));
                    }
                }
            }
        });
    }

    @Override
    public boolean shouldUseDefaultConfig() {
        return true;
    }

    @Override
    public List<Component> memeps() {
        return List.of(Component.text("When you tap someone on the shoulder on the left but you're at the right:").color(NamedTextColor.GOLD));
    }

    @EventListener
    public void onReload(ReloadEvent e) {
        try {
            handleDefaultReloadElseThrow();
            e.showValidReload(this);
        } catch (ConfigurateException ex) {
            e.presentIssue(this, "Unable to reload config, please check console!");
            getLogger().info(ExceptionUtils.throwableToString(ex));
        }
    }
}
