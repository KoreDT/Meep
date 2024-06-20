/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.kore.meep.api.Meep;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.positioning.CanReturnRelative;
import net.kore.meep.api.positioning.Coordinates;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PositionArgumentType implements ArgumentType<Coordinates> {
    public static final SimpleCommandExceptionType ERROR_EXPECTED_DOUBLE = new SimpleCommandExceptionType(() -> "Excepted a coordinate");
    public static final SimpleCommandExceptionType ERROR_MIXED_TYPE = new SimpleCommandExceptionType(() -> "Cannot mix world & local coordinates (everything must either use ^/~ or not)");
    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(() -> "Incomplete (expected 3 coordinates)");
    public static final SimpleCommandExceptionType ERROR_NO_PLAYER_FOUND = new SimpleCommandExceptionType(() -> "No player found");
    public static final SimpleCommandExceptionType ERROR_NO_EXACT_COORDS_ALLOWED = new SimpleCommandExceptionType(() -> "Exact coordinates are required for this parameter");

    private final boolean allowOnlyExact;
    private final boolean allowPlayer;

    protected PositionArgumentType(boolean allowOnlyExact, boolean allowPlayer) {
        this.allowOnlyExact = allowOnlyExact;
        this.allowPlayer = allowPlayer;
    }

    @Override
    @CanReturnRelative
    public Coordinates parse(StringReader stringReader) throws CommandSyntaxException {
        int i = stringReader.getCursor();
        if (stringReader.readStringUntil(' ').equals("player") && allowPlayer) {
            String playerName = stringReader.readUnquotedString();
            try {
                UUID uuid = UUID.fromString(playerName);
                Player p = Meep.get().getPlayer(uuid);
                if (p != null) return p.getPosition().getCoordinates();
            } catch (Throwable ignored) {}

            for (Player p : Meep.get().getOnlinePlayers()) {
                if (p.getName().equals(playerName)) return p.getPosition().getCoordinates();
            }

            throw ERROR_NO_PLAYER_FOUND.createWithContext(stringReader);
        }
        stringReader.setCursor(i);

        return stringReader.canRead() && (stringReader.peek() == '^' || stringReader.peek() == '~')
                ? parseRelative(stringReader)
                : parseWorld(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!allowPlayer) return Suggestions.empty();
        String arg = builder.getRemaining();
        if (arg.isEmpty()) {
            return builder.suggest("player").buildFuture();
        } else {
            List<String> args = List.of(arg.split(" "));
            if (args.getFirst().equals("player")) {
                for (Player p : Meep.get().getOnlinePlayers()) {
                    if (p.getName().startsWith(builder.getRemaining())) {
                        builder.suggest("player " + p.getName());
                    }
                }
                return builder.buildFuture();
            }
            return Suggestions.empty();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("~ ~ ~", "0 0 0", "player Novampr");
    }

    public Coordinates parseRelative(StringReader reader) throws CommandSyntaxException {
        if (allowOnlyExact) throw ERROR_NO_EXACT_COORDS_ALLOWED.createWithContext(reader);
        int i = reader.getCursor();
        Coordinates.RelativeData d = readRelativeDouble(reader, i);
        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            Coordinates.RelativeData e = readRelativeDouble(reader, i);
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                Coordinates.RelativeData f = readRelativeDouble(reader, i);
                try {
                    return new Coordinates(d, e, f);
                } catch (IllegalArgumentException ex) {
                    throw ERROR_MIXED_TYPE.createWithContext(reader);
                }
            } else {
                reader.setCursor(i);
                throw ERROR_NOT_COMPLETE.createWithContext(reader);
            }
        } else {
            reader.setCursor(i);
            throw ERROR_NOT_COMPLETE.createWithContext(reader);
        }
    }

    public Coordinates parseWorld(StringReader reader) throws CommandSyntaxException {
        int i = reader.getCursor();
        double d = readWorldDouble(reader, i);
        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            double e = readWorldDouble(reader, i);
            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                double f = readWorldDouble(reader, i);
                return new Coordinates(d, e, f);
            } else {
                reader.setCursor(i);
                throw ERROR_NOT_COMPLETE.createWithContext(reader);
            }
        } else {
            reader.setCursor(i);
            throw ERROR_NOT_COMPLETE.createWithContext(reader);
        }
    }

    private Coordinates.RelativeData readRelativeDouble(StringReader reader, int startingCursorPos) throws CommandSyntaxException {
        if (!reader.canRead()) {
            throw ERROR_EXPECTED_DOUBLE.createWithContext(reader);
        } else if (reader.peek() != '^' && reader.peek() != '~') {
            reader.setCursor(startingCursorPos);
            throw ERROR_MIXED_TYPE.createWithContext(reader);
        } else {
            Coordinates.RelativeType t = switch (reader.peek()) {
                case '^' -> Coordinates.RelativeType.DIRECTIONAL;
                case '~' -> Coordinates.RelativeType.POSITIONAL;
                default -> throw new IllegalStateException("Unexpected value: " + reader.peek());
            };
            reader.skip();
            return new Coordinates.RelativeData(reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0, t);
        }
    }

    private double readWorldDouble(StringReader reader, int startingCursorPos) throws CommandSyntaxException {
        if (!reader.canRead()) {
            throw ERROR_EXPECTED_DOUBLE.createWithContext(reader);
        } else if (reader.peek() == '^' || reader.peek() == '~') {
            reader.setCursor(startingCursorPos);
            throw ERROR_MIXED_TYPE.createWithContext(reader);
        } else {
            reader.skip();
            return reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0;
        }
    }
}
