/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.entity.types;

import net.kyori.adventure.bossbar.BossBar;

public interface Boss {
    /**
     * Get the boss bar of the current boss
     * @return {@link BossBar}
     */
    BossBar getBossBar();

    /**
     * Set the boss bar of the current boss
     * @param bossBar The boss bar
     */
    void setBossBar(BossBar bossBar);
}
