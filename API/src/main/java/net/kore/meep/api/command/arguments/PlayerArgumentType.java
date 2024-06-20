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
import net.kore.meep.api.Meep;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.utils.CommandUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PlayerArgumentType implements ArgumentType<Player> {
    @Override
    public Player parse(StringReader reader) throws CommandSyntaxException {
        String name = reader.readUnquotedString();
        for (Player p : Collections.unmodifiableList(Meep.get().getOnlinePlayers())) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        try {
            return Meep.get().getPlayer(UUID.fromString(name));
        } catch (IllegalArgumentException e) {
            throw CommandUtils.createException("No player found.");
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining();
        for (Player p : Collections.unmodifiableList(Meep.get().getOnlinePlayers())) {
            if (p.getName().startsWith(input)) {
                builder.suggest(p.getName());
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("Novampr", "SlimeDudeGamer", "Notch");
    }
}
