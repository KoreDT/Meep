/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event.lifecycle;

import net.kore.meep.api.event.Event;

public class EnableEvent extends Event {
    private final String platform;
    public String getPlatform() {
        return platform;
    }

    public EnableEvent(String platform) {
        this.platform = platform;
    }
}
