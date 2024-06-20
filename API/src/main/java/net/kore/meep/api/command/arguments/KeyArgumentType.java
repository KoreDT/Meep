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
import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.utils.CommandUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KeyArgumentType implements ArgumentType<NamespaceKey> {
    private final List<NamespaceKey> keys;

    private KeyArgumentType(List<NamespaceKey> keys) {
        this.keys = keys;
    }

    protected static KeyArgumentType block() {
        return new KeyArgumentType(Collections.unmodifiableList(Meep.get().getKnownBlockKeys()));
    }
    protected static KeyArgumentType item() {
        return new KeyArgumentType(Collections.unmodifiableList(Meep.get().getKnownItemKeys()));
    }
    protected static KeyArgumentType entity() {
        return new KeyArgumentType(Collections.unmodifiableList(Meep.get().getKnownEntityKeys()));
    }
    protected static KeyArgumentType enchant() {
        return new KeyArgumentType(Collections.unmodifiableList(Meep.get().getKnownEnchantKeys()));
    }

    @Override
    public NamespaceKey parse(StringReader reader) throws CommandSyntaxException {
        String strKey = reader.readUnquotedString();
        for (NamespaceKey key : keys) {
            if (key.getNamespace().equals("minecraft")) {
                if (key.getFormatted().equals(strKey) || key.getPath().equals(strKey)) return key;
            } else {
                if (key.getFormatted().equals(strKey)) return key;
            }
        }

        throw CommandUtils.createException("Unable to find key.");
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = builder.getRemaining();
        for (NamespaceKey key : keys) {
            if (key.getNamespace().equals("minecraft")) {
                if (key.getFormatted().startsWith(input) || key.getPath().startsWith(input)) {
                    builder.suggest(key.getFormatted());
                }
            } else {
                if (key.getFormatted().startsWith(input)) {
                    builder.suggest(key.getFormatted());
                }
            }
        }
        return builder.buildFuture();
    }

    @Override
    public Collection<String> getExamples() {
        return List.of("Notch", "Novampr", "SlimeDudeGamer");
    }
}
