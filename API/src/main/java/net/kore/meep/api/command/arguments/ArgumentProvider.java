/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.kore.meep.api.NamespaceKey;
import net.kore.meep.api.color.DyeColor;
import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.entity.Player;
import net.kore.meep.api.meepling.Meepling;
import net.kore.meep.api.positioning.Coordinates;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import java.util.List;

public class ArgumentProvider {
    /**
     * Create a literal argument
     * @param name The name of the literal
     * @return {@link LiteralArgumentBuilder}<{@link CommandSender}>
     */
    public static LiteralArgumentBuilder<CommandSender> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Create a literal argument with a permission
     * @param name The name of the literal
     * @param permission The permission needed to run this node
     * @return {@link LiteralArgumentBuilder}<{@link CommandSender}>
     */
    public static LiteralArgumentBuilder<CommandSender> literal(String name, String permission) {
        return literal(name).requires(sender -> sender.hasPermission(permission));
    }

    /**
     * Create an argument typed argument
     * @param name The name of the argument
     * @param type The type of the argument, handles parsing
     * @return {@link RequiredArgumentBuilder}<{@link CommandSender}, {@link T}>
     * @param <T> The type of the argument which is returned
     */
    public static <T> RequiredArgumentBuilder<CommandSender, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    /**
     * Create an argument typed argument with a permission
     * @param name The name of the argument
     * @param type The type of the argument, handles parsing
     * @param permission The permission needed to run this node
     * @return {@link RequiredArgumentBuilder}<{@link CommandSender}, {@link T}>
     * @param <T> The type of the argument which is returned
     */
    public static <T> RequiredArgumentBuilder<CommandSender, T> argument(String name, ArgumentType<T> type, String permission) {
        return argument(name, type).requires(sender -> sender.hasPermission(permission));
    }

    /**
     * Get a player argument type
     * @return {@link ArgumentType}<{@link Player}>
     */
    public static ArgumentType<Player> player() {
        return new PlayerArgumentType();
    }

    /**
     * Get a multi-player argument type
     * @return {@link ArgumentType}<{@link List}<{@link Player}>>
     */
    public static ArgumentType<List<Player>> players() {
        return new PlayersArgumentType();
    }

    /**
     * Get a block argument type
     * @return {@link ArgumentType}<{@link NamespaceKey}>
     */
    public static ArgumentType<NamespaceKey> block() {
        return KeyArgumentType.block();
    }

    /**
     * Get a item argument type
     * @return {@link ArgumentType}<{@link NamespaceKey}>
     */
    public static ArgumentType<NamespaceKey> item() {
        return KeyArgumentType.item();
    }

    /**
     * Get a entity argument type
     * @return {@link ArgumentType}<{@link NamespaceKey}>
     */
    public static ArgumentType<NamespaceKey> entity() {
        return KeyArgumentType.entity();
    }

    /**
     * Get a position argument type but it allows a Player as an input
     * @return {@link ArgumentType}<{@link Coordinates}>
     */
    public static ArgumentType<Coordinates> position() {
        return new PositionArgumentType(false, true);
    }

    /**
     * Get a position argument type
     * @return {@link ArgumentType}<{@link Coordinates}>
     */
    public static ArgumentType<Coordinates> positionNotPlayer() {
        return new PositionArgumentType(false, false);
    }

    /**
     * Get an exact position argument type but it allows a Player as an input
     * @return {@link ArgumentType}<{@link Coordinates}>
     */
    public static ArgumentType<Coordinates> exactPosition() {
        return new PositionArgumentType(true, true);
    }

    /**
     * Get an exact position argument type
     * @return {@link ArgumentType}<{@link Coordinates}>
     */
    public static ArgumentType<Coordinates> exactPositionNoPlayer() {
        return new PositionArgumentType(true, false);
    }

    /**
     * Get a color argument type
     * @return {@link ArgumentType}<{@link TextColor}>
     */
    public static ArgumentType<TextColor> color() {
        return new ColorArgumentType();
    }

    /**
     * Get a named color argument type
     * @return {@link ArgumentType}<{@link NamedTextColor}>
     */
    public static ArgumentType<NamedTextColor> namedColor() {
        return new NamedColorArgumentType();
    }

    /**
     * Get a dye color argument type
     * @return {@link ArgumentType}<{@link DyeColor}>
     */
    public static ArgumentType<DyeColor> dyeColor() {
        return new DyeColorArgumentType();
    }

    /**
     * Get a Meepling argument type
     * @return {@link ArgumentType}<{@link Meepling}>
     */
    public static ArgumentType<Meepling> meepling() {
        return new MeeplingArgumentType();
    }
}
