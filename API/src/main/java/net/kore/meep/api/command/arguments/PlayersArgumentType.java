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
import net.kore.meep.api.positioning.Coordinates;
import net.kore.meep.api.positioning.Vector;
import net.kore.meep.api.utils.CommandUtils;
import net.kore.meep.api.utils.PlayerUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class PlayersArgumentType implements ArgumentType<List<Player>> {
    @Override
    public List<Player> parse(StringReader reader) throws CommandSyntaxException {
        String input = reader.readUnquotedString();
        if (input.equals("@a")) {
            return Collections.unmodifiableList(Meep.get().getOnlinePlayers());
        }
        String[] names = input.split(",");
        List<Player> players = new ArrayList<>();
        for (String name : names) {
            for (Player p : Collections.unmodifiableList(Meep.get().getOnlinePlayers())) {
                if (p.getName().equals(name)) {
                    players.add(p);
                } else {
                    try {
                        players.add(Meep.get().getPlayer(UUID.fromString(name)));
                    } catch (IllegalArgumentException ignored) {}
                }
            }
        }


        if (!players.isEmpty()) {
            return players;
        } else {
            throw CommandUtils.createException("No players found.");
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String fullInput = builder.getRemaining();
        List<String> last = Arrays.stream(fullInput.split(",")).toList();
        String input = last.get(last.size() - 1);
        for (Player p : Collections.unmodifiableList(Meep.get().getOnlinePlayers())) {
            if (p.getName().startsWith(input)) {
                builder.suggest(fullInput + p.getName().substring(input.length()));
            } else if (p.uuid().toString().startsWith(input) && context.getSource() instanceof Player pl && PlayerUtils.isLookingAt(pl, p)) {
                builder.suggest(fullInput + p.uuid().toString().substring(input.length()));
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("Novampr", "SlimeDudeGamer", "Notch");
    }
}
