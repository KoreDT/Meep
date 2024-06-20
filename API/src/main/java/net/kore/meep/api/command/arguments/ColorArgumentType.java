/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kore.meep.api.color.DyeColor;
import net.kore.meep.api.utils.CommandUtils;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ColorArgumentType implements ArgumentType<TextColor> {
    private final Map<String, TextColor> suggestables = new HashMap<>();

    protected ColorArgumentType() {
        for (DyeColor color : DyeColor.values()) {
            suggestables.put("dye:"+color.name().toLowerCase(Locale.ENGLISH), color.getAdventureColor());
        }
        suggestables.put("named:black", NamedTextColor.BLACK);
        suggestables.put("named:dark_blue", NamedTextColor.DARK_BLUE);
        suggestables.put("named:dark_green", NamedTextColor.DARK_GREEN);
        suggestables.put("named:dark_aqua", NamedTextColor.DARK_AQUA);
        suggestables.put("named:dark_red", NamedTextColor.DARK_RED);
        suggestables.put("named:dark_purple", NamedTextColor.DARK_PURPLE);
        suggestables.put("named:gold", NamedTextColor.GOLD);
        suggestables.put("named:gray", NamedTextColor.GRAY);
        suggestables.put("named:dark_gray", NamedTextColor.DARK_GRAY);
        suggestables.put("named:blue", NamedTextColor.BLUE);
        suggestables.put("named:green", NamedTextColor.GREEN);
        suggestables.put("named:aqua", NamedTextColor.AQUA);
        suggestables.put("named:red", NamedTextColor.RED);
        suggestables.put("named:light_purple", NamedTextColor.LIGHT_PURPLE);
        suggestables.put("named:yellow", NamedTextColor.YELLOW);
        suggestables.put("named:white", NamedTextColor.WHITE);
    }

    @Override
    public TextColor parse(StringReader stringReader) throws CommandSyntaxException {
        String color = stringReader.readUnquotedString();
        if (suggestables.get(color) != null) return suggestables.get(color);
        TextColor textColor = TextColor.fromCSSHexString(color);
        if (textColor != null) return textColor;
        throw CommandUtils.createException("Unknown color, unable to parse.");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining();
        for (String suggestion : suggestables.keySet()) {
            if (suggestion.startsWith(input)) {
                builder.suggest(suggestion);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("dye:magenta", "named:aqua", "#ffffff", "#000");
    }
}
