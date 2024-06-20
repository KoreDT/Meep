package net.kore.meep.fabric;

import net.kore.meep.api.Console;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

public class FabricConsole implements Console {
    @Override
    public void executeCommand(String command) {
        MeepFabric.getServer().getCommands().performPrefixedCommand(MeepFabric.getServer().createCommandSourceStack(), command);
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        MeepFabric.LOGGER.info(PlainTextComponentSerializer.plainText().serialize(message));
    }
}
