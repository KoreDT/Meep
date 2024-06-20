/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.entity;

import net.kore.meep.api.permission.Permission;
import net.kore.meep.paper.utils.MeepToBukkit;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.resource.ResourcePackInfoLike;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.resource.ResourcePackRequestLike;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.advancement.Advancement;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class Player extends Entity implements net.kore.meep.api.entity.Player {
    private final org.bukkit.entity.Player parent;
    public Player(org.bukkit.entity.Player p) {
        super(p);
        parent = p;
    }

    @Override
    public java.util.UUID getUUID() {
        return parent.getUniqueId();
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public Component displayName() {
        return parent.displayName();
    }

    @Override
    public void displayName(Component name) {
        parent.displayName(name);
    }

    @Override
    public void kick() {
        kick(Component.text("You have been kicked from the server."));
    }

    @Override
    public void kick(Component reason) {
        parent.kick(reason);
    }

    @Override
    public void ban() {
        ban(Component.text("You have been banned from the server."));
    }

    @Override
    public void ban(Component reason) {
        kick(reason);
        parent.ban(PlainTextComponentSerializer.plainText().serialize(reason), Instant.MAX, "Meep");
    }

    @Override
    public boolean isSneaking() {
        return parent.isSneaking();
    }

    @Override
    public boolean isSprinting() {
        return parent.isSprinting();
    }

    @Override
    public java.util.@NotNull UUID uuid() {
        return getUUID();
    }

    @Override
    public void sendMessage(@NotNull Component message, ChatType.@NotNull Bound boundChatType) {
        parent.sendMessage(message, boundChatType);
    }

    @Override
    public void sendMessage(@NotNull ComponentLike message, ChatType.@NotNull Bound boundChatType) {
        parent.sendMessage(message, boundChatType);
    }

    @Override
    public void sendMessage(@NotNull SignedMessage signedMessage, ChatType.@NotNull Bound boundChatType) {
        parent.sendMessage(signedMessage, boundChatType);
    }

    @Override
    public void deleteMessage(@NotNull SignedMessage signedMessage) {
        parent.deleteMessage(signedMessage);
    }

    @Override
    public void deleteMessage(SignedMessage.@NotNull Signature signature) {
        parent.deleteMessage(signature);
    }

    @Override
    public void sendActionBar(@NotNull ComponentLike message) {
        parent.sendActionBar(message);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        parent.sendActionBar(message);
    }

    @Override
    public void sendPlayerListHeader(@NotNull ComponentLike header) {
        parent.sendPlayerListHeader(header);
    }

    @Override
    public void sendPlayerListHeader(@NotNull Component header) {
        parent.sendPlayerListHeader(header);
    }

    @Override
    public void sendPlayerListFooter(@NotNull ComponentLike footer) {
        parent.sendPlayerListFooter(footer);
    }

    @Override
    public void sendPlayerListFooter(@NotNull Component footer) {
        parent.sendPlayerListFooter(footer);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull ComponentLike header, @NotNull ComponentLike footer) {
        parent.sendPlayerListHeaderAndFooter(header, footer);
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NotNull Component header, @NotNull Component footer) {
        parent.sendPlayerListHeaderAndFooter(header, footer);
    }

    @Override
    public void showTitle(@NotNull Title title) {
        parent.showTitle(title);
    }

    @Override
    public <T> void sendTitlePart(@NotNull TitlePart<T> part, @NotNull T value) {
        parent.sendTitlePart(part, value);
    }

    @Override
    public void clearTitle() {
        parent.clearTitle();
    }

    @Override
    public void resetTitle() {
        parent.resetTitle();
    }

    @Override
    public void showBossBar(@NotNull BossBar bar) {
        parent.showBossBar(bar);
    }

    @Override
    public void hideBossBar(@NotNull BossBar bar) {
        parent.hideBossBar(bar);
    }

    @Override
    public void playSound(@NotNull Sound sound) {
        parent.playSound(sound);
    }

    @Override
    public void playSound(@NotNull Sound sound, double x, double y, double z) {
        parent.playSound(sound, x, y, z);
    }

    @Override
    public void playSound(@NotNull Sound sound, Sound.@NotNull Emitter emitter) {
        parent.playSound(sound, emitter);
    }

    @Override
    public void stopSound(@NotNull Sound sound) {
        parent.stopSound(sound);
    }

    @Override
    public void stopSound(@NotNull SoundStop stop) {
        parent.stopSound(stop);
    }

    @Override
    public void openBook(Book.@NotNull Builder book) {
        parent.openBook(book);
    }

    @Override
    public void openBook(@NotNull Book book) {
        parent.openBook(book);
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackInfoLike first, @NotNull ResourcePackInfoLike... others) {
        parent.sendResourcePacks(first, others);
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequestLike request) {
        parent.sendResourcePacks(request);
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        parent.sendResourcePacks(request);
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackRequestLike request) {
        parent.removeResourcePacks(request);
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackRequest request) {
        parent.removeResourcePacks(request);
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackInfoLike request, @NotNull ResourcePackInfoLike @NotNull ... others) {
        parent.removeResourcePacks(request, others);
    }

    @Override
    public void removeResourcePacks(@NotNull Iterable<java.util.UUID> ids) {
        parent.removeResourcePacks(ids);
    }

    @Override
    public void removeResourcePacks(java.util.@NotNull UUID id, java.util.@NotNull UUID @NotNull ... others) {
        parent.removeResourcePacks(id, others);
    }

    @Override
    public void clearResourcePacks() {
        parent.clearResourcePacks();
    }

    @Override
    public void addPermission(Permission permission) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getPlayerAdapter(org.bukkit.entity.Player.class).getUser(parent);
        user.data().add(PermissionNode.builder(permission.node()).build());
        luckPerms.getUserManager().saveUser(user);
    }

    @Override
    public void removePermission(Permission permission) {
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getPlayerAdapter(org.bukkit.entity.Player.class).getUser(parent);
        user.data().remove(PermissionNode.builder(permission.node()).build());
        luckPerms.getUserManager().saveUser(user);
    }

    @Override
    public boolean hasPermission(Permission permission) {
        return parent.hasPermission(MeepToBukkit.permission(permission));
    }

    @Override
    public boolean hasPermission(String permission) {
        return parent.hasPermission(permission);
    }

    @Override
    public Set<Permission> getPermissions() {
        Set<Permission> permissions = new HashSet<>();
        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getPlayerAdapter(org.bukkit.entity.Player.class).getUser(parent);
        for (Node node : user.data().toCollection()) {
            permissions.add(new Permission(node.getKey(), null, null));
        }
        return permissions;
    }
}
