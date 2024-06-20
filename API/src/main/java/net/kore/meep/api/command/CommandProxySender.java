/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.command;

import net.kore.meep.api.permission.Permission;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Create a proxied {@link CommandSender}, mainly used by the execute command in Minecraft
 * @param caller
 * @param callee
 */
public record CommandProxySender(CommandSender caller, CommandSender callee) implements CommandSender {

    @Override
    public void sendMessage(@NotNull Component message) {
        callee.sendMessage(message);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        callee.sendActionBar(message);
    }

    @Override
    public void sendPlayerListHeader(@NotNull Component header) {
        callee.sendPlayerListHeader(header);
    }

    @Override
    public void sendPlayerListFooter(@NotNull Component footer) {
        callee.sendPlayerListFooter(footer);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        callee.sendPlayerListHeaderAndFooter(header, footer);
    }

    @Override
    public void showTitle(@NotNull Title title) {
        callee.showTitle(title);
    }

    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        callee.sendTitlePart(part, value);
    }

    @Override
    public void clearTitle() {
        callee.clearTitle();
    }

    @Override
    public void resetTitle() {
        callee.resetTitle();
    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {
        callee.showBossBar(bar);
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        callee.hideBossBar(bar);
    }

    @Override
    public void playSound(@NotNull Sound sound) {
        callee.playSound(sound);
    }

    @Override
    public void playSound(@NotNull Sound sound, double x, double y, double z) {
        callee.playSound(sound, x, y, z);
    }

    @Override
    public void playSound(@NotNull Sound sound, Sound.Emitter emitter) {
        callee.playSound(sound, emitter);
    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        callee.stopSound(sound);
    }

    @Override
    public void stopSound(@NotNull SoundStop stop) {
        callee.stopSound(stop);
    }

    @Override
    public void openBook(Book.Builder book) {
        callee.openBook(book);
    }

    @Override
    public void openBook(@NotNull Book book) {
        callee.openBook(book);
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        callee.sendResourcePacks(request);
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackRequest request) {
        callee.removeResourcePacks(request);
    }

    @Override
    public void removeResourcePacks(@NotNull Iterable<UUID> ids) {
        callee.removeResourcePacks(ids);
    }

    @Override
    public void removeResourcePacks(@NotNull UUID id, @NotNull UUID @NotNull ... others) {
        callee.removeResourcePacks(id, others);
    }

    @Override
    public void clearResourcePacks() {
        callee.clearResourcePacks();
    }

    @Override
    public void addPermission(Permission permission) {
        callee.addPermission(permission);
    }

    @Override
    public void removePermission(Permission permission) {
        callee.removePermission(permission);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return callee.hasPermission(permission);
    }

    @Override
    public boolean hasPermission(String permission) {
        return callee.hasPermission(permission);
    }

    @Override
    public Set<Permission> getPermissions() {
        return callee.getPermissions();
    }
}
