/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.color;

import net.kyori.adventure.text.format.TextColor;

public enum DyeColor {
    RED(TextColor.color(11546150)),
    ORANGE(TextColor.color(16351261)),
    YELLOW(TextColor.color(16701501)),
    LIME(TextColor.color(8439583)),
    GREEN(TextColor.color(6192150)),
    CYAN(TextColor.color(1481884)),
    LIGHT_BLUE(TextColor.color(3847130)),
    BLUE(TextColor.color(3949738)),
    PURPLE(TextColor.color(8991416)),
    MAGENTA(TextColor.color(13061821)),
    PINK(TextColor.color(15961002)),
    WHITE(TextColor.color(16383998)),
    LIGHT_GRAY(TextColor.color(10329495)),
    GRAY(TextColor.color(4673362)),
    BLACK(TextColor.color(1908001)),
    BROWN(TextColor.color(8606770));

    private final TextColor color;

    DyeColor(TextColor color) {
        this.color = color;
    }

    public TextColor getAdventureColor() {
        return color;
    }
}
