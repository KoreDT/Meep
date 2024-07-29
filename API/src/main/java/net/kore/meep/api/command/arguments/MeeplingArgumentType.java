/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kore.meep.api.meepling.Meepling;
import net.kore.meep.api.meepling.MeeplingManager;
import net.kore.meep.api.utils.CommandUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MeeplingArgumentType implements ArgumentType<Meepling> {
    protected MeeplingArgumentType () {}

    @Override
    public Meepling parse(StringReader stringReader) throws CommandSyntaxException {
        Optional<Meepling> meepling = MeeplingManager.get().getMeepling(stringReader.readQuotedString());
        if (meepling.isEmpty()) throw CommandUtils.createException("No meepling found.");
        return meepling.get();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining();
        for (Meepling meepling : MeeplingManager.get().getMeeplings()) {
            if (meepling.getName().startsWith(input)) {
                builder.suggest(meepling.getName());
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("\"Example Meepling 1\"", "\"Example Meepling 2\"");
    }
}
