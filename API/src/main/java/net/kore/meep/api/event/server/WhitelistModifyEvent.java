/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.server;

import net.kore.meep.api.WhitelistManager;
import net.kore.meep.api.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class WhitelistModifyEvent extends Event {
    private final WhitelistManager manager;

    /**
     * Get the whitelist manager
     * @return {@link WhitelistManager}
     */
    public WhitelistManager getWhitelistManager() {
        return manager;
    }

    private final @Nullable UUID uuid;

    /**
     * Get the possible UUID with this event
     * @return {@link UUID}
     */
    public @Nullable UUID getUUID() {
        return uuid;
    }

    private final Action action;

    /**
     * Get the action
     * @return {@link Action}
     */
    public Action getAction() {
        return action;
    }

    public WhitelistModifyEvent(WhitelistManager manager, @Nullable UUID uuid, Action action) {
        this.manager = manager;
        this.uuid = uuid;
        this.action = action;
    }

    public enum Action {
        ADD,
        REMOVE,
        CHECK,
        ENABLE,
        DISABLE,
        KICK_UNPERMITTED
    }
}
