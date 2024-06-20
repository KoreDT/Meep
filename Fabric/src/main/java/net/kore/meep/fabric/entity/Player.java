package net.kore.meep.fabric.entity;

import com.mojang.authlib.GameProfile;
import net.kore.meep.fabric.MeepFabric;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.platform.fabric.FabricAudiences;
import net.kyori.adventure.resource.ResourcePackRequest;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserBanListEntry;
import org.jetbrains.annotations.NotNull;

public class Player extends Entity implements net.kore.meep.api.entity.Player {
    private ServerPlayer parent;

    public Player(ServerPlayer parent) {
        super(parent);
        this.parent = parent;
    }

    @Override
    public Component displayName() {
        if (parent.getDisplayName() == null) return null;
        return FabricAudiences.nonWrappingSerializer().deserialize(parent.getDisplayName());
    }

    @Override
    public void displayName(Component name) {
        parent.getTeam().setDisplayName(FabricAudiences.nonWrappingSerializer().serialize(name));
    }

    @Override
    public void kick() {
        parent.connection.disconnect(net.minecraft.network.chat.Component.translatable("multiplayer.disconnect.kicked"));
    }

    @Override
    public void kick(Component reason) {
        parent.connection.disconnect(FabricAudiences.nonWrappingSerializer().serialize(reason));
    }

    @Override
    public void ban() {
        MeepFabric.getServer().getPlayerList().getBans().add(new UserBanListEntry(new GameProfile(getUUID(), getName())));
        parent.connection.disconnect(net.minecraft.network.chat.Component.translatable("multiplayer.disconnect.banned"));
    }

    @Override
    public void ban(Component reason) {
        MeepFabric.getServer().getPlayerList().getBans().add(new UserBanListEntry(new GameProfile(getUUID(), getName())));
        parent.connection.disconnect(FabricAudiences.nonWrappingSerializer().serialize(reason));
    }

    @Override
    public boolean isSneaking() {
        return parent.isCrouching();
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
    public void sendMessage(@NotNull Component message) {
        parent.sendMessage(message);
    }

    @Override
    public void sendActionBar(@NotNull Component message) {
        parent.sendActionBar(message);
    }

    @Override
    public void sendPlayerListHeader(@NotNull Component header) {
        parent.sendPlayerListHeader(header);
    }

    @Override
    public void sendPlayerListFooter(@NotNull Component footer) {
        parent.sendPlayerListFooter(footer);
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
    public void stopSound(@NotNull Sound sound) {
        parent.stopSound(sound);
    }

    @Override
    public void stopSound(@NotNull SoundStop stop) {
        parent.stopSound(stop);
    }

    @Override
    public void openBook(@NotNull Book book) {
        parent.openBook(book);
    }

    @Override
    public void sendResourcePacks(@NotNull ResourcePackRequest request) {
        parent.sendResourcePacks(request);
    }

    @Override
    public void removeResourcePacks(@NotNull ResourcePackRequest request) {
        parent.removeResourcePacks(request);
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
    public void openBook(Book.Builder book) {
        parent.openBook(book);
    }

    @Override
    public void playSound(@NotNull Sound sound, Sound.Emitter emitter) {
        parent.playSound(sound, emitter);
    }
}
