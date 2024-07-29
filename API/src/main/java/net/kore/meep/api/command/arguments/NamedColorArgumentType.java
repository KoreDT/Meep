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

public class 1NamedColorArgumentType implements ArgumentType<NamedTextColor> {
    private final Map<String, NamedTextColor> suggestables = new HashMap<>();

    protected NamedColorArgumentType() {
        suggestables.put("black", NamedTextColor.BLACK);
        suggestables.put("dark_blue", NamedTextColor.DARK_BLUE);
        suggestables.put("dark_green", NamedTextColor.DARK_GREEN);
        suggestables.put("dark_aqua", NamedTextColor.DARK_AQUA);
        suggestables.put("dark_red", NamedTextColor.DARK_RED);
        suggestables.put("dark_purple", NamedTextColor.DARK_PURPLE);
        suggestables.put("gold", NamedTextColor.GOLD);
        suggestables.put("gray", NamedTextColor.GRAY);
        suggestables.put("dark_gray", NamedTextColor.DARK_GRAY);
        suggestables.put("blue", NamedTextColor.BLUE);
        suggestables.put("green", NamedTextColor.GREEN);
        suggestables.put("aqua", NamedTextColor.AQUA);
        suggestables.put("red", NamedTextColor.RED);
        suggestables.put("light_purple", NamedTextColor.LIGHT_PURPLE);
        suggestables.put("yellow", NamedTextColor.YELLOW);
        suggestables.put("white", NamedTextColor.WHITE);
    }

    @Override
    public NamedTextColor parse(StringReader stringReader) throws CommandSyntaxException {
        String color = stringReader.readUnquotedString();
        if (suggestables.get(color) != null) return suggestables.get(color);

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
        return List.of("magenta", "aqua");
    }
}
