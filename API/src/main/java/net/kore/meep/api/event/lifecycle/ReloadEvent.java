/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.lifecycle;

import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.event.AfterEvent;
import net.kore.meep.api.event.Event;
import net.kore.meep.api.meepling.Meepling;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.HashMap;
import java.util.Map;

public class ReloadEvent extends Event implements AfterEvent {
    private final CommandSender commandSender;
    public CommandSender getCommandSender() {
        return commandSender;
    }
    private final Map<Meepling, String> issues = new HashMap<>();
    public void presentIssue(Meepling meepling, String issue) {
        issues.put(meepling, issue);
    }
    public void showValidReload(Meepling meepling) {
        commandSender.sendMessage(Component.text(meepling.getName() + " has reload successfully!").color(NamedTextColor.GREEN));
    }

    public ReloadEvent(CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    @Override
    public void afterEvent() {
        if (issues.isEmpty()) {
            commandSender.sendMessage(Component.text("Meeplings reloaded successfully!").color(NamedTextColor.GREEN));
        } else {
            Component base = Component.text("Some meeplings ran into an issue loading their configs (others may have loaded fine):").color(NamedTextColor.RED);
            for (Map.Entry<Meepling, String> entry : issues.entrySet()) {
                base = base.append(Component.text("\n- "+entry.getKey().getName()+":\n    "+entry.getValue()).color(NamedTextColor.RED));
            }
            commandSender.sendMessage(base);
        }
    }
}
