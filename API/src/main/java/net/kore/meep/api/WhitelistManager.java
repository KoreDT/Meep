/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api;

import java.util.UUID;

public interface WhitelistManager {
    void add(UUID uuid);
    void remove(UUID uuid);
    boolean inList(UUID uuid);
    void enable();
    void disable();
    void kickAllUnpermittedPlayers();
}
