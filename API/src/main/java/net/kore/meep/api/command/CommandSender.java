/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command;

import net.kore.meep.api.permission.Permissible;
import net.kyori.adventure.audience.Audience;

/**
 * An entity or other item which can send a command.
 */
public interface CommandSender extends Audience, Permissible {
}
