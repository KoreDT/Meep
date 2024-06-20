/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api;

import net.kore.meep.api.command.AllPermissiveCommandSender;

public interface Console extends AllPermissiveCommandSender {
    /**
     * Execute a command as console
     * @param command The command
     */
    void executeCommand(String command);
}
