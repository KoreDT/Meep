package net.kore.meep.api.format;

import net.kore.meep.api.Meep;
import net.kore.meep.api.entity.Entity;
import net.kore.meep.api.world.World;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Formatter {
    public Formatter() {
        Meep.getMasterLogger().info(PlainTextComponentSerializer.plainText().serialize(displayName()) + " has been enabled!");
    }

    public abstract @NotNull String id();

    public @NotNull Component displayName() {
        return Component.text(id()).color(NamedTextColor.GREEN);
    }

    public @Nullable String version() {
        return null;
    }

    public boolean persistOnReload() {
        return false;
    }

    public abstract @Nullable String parse(String id, Entity entity, World world);
}
