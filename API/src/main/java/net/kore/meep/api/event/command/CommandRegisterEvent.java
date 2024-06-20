/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.command;

import com.mojang.brigadier.CommandDispatcher;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.event.Event;

public class CommandRegisterEvent extends Event {
    private final CommandDispatcher<CommandSender> commandDispatcher;
    public CommandDispatcher<CommandSender> getCommandDispatcher() {
        return commandDispatcher;
    }

    public CommandRegisterEvent() {
        this.commandDispatcher = new CommandDispatcher<>();
    }
}
