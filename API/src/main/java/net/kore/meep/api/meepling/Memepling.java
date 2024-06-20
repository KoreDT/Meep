/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.meepling;

import net.kyori.adventure.text.Component;

import java.util.List;

public interface Memepling {
    /**
     * Get memes for the official meme API (Yes, I did it.)
     * @return {@link List}<{@link Component}>
     */
    List<Component> memeps();
}
