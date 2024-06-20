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

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class DyeColorArgumentType implements ArgumentType<DyeColor> {
    protected DyeColorArgumentType() {}

    @Override
    public DyeColor parse(StringReader stringReader) throws CommandSyntaxException {
        String color = stringReader.readUnquotedString();
        for (DyeColor dyeColor : DyeColor.values()) {
            if (dyeColor.name().toLowerCase(Locale.ENGLISH).equals(color)) return dyeColor;
        }

        throw CommandUtils.createException("Unknown color, unable to parse.");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining();
        for (DyeColor color : DyeColor.values()) {
            String suggestion = color.name().toLowerCase(Locale.ENGLISH);
            if (suggestion.startsWith(input)) {
                builder.suggest(suggestion);
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("red", "light_blue");
    }
}
