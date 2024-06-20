package net.kore.meep.fabric;

import net.kore.meep.api.WhitelistManager;
import net.kore.meep.api.event.EventManager;
import net.kore.meep.api.event.server.WhitelistModifyEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.KickCommand;
import net.minecraft.server.commands.WhitelistCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.UserWhiteListEntry;

import java.util.UUID;

public class FabricWhitelistManager implements WhitelistManager {
    @Override
    public void add(UUID uuid) {
        ServerPlayer player = MeepFabric.getServer().getPlayerList().getPlayer(uuid);
        if (player == null) {
            return;
        }

        MeepFabric.getServer().getPlayerList().getWhiteList().add(new UserWhiteListEntry(player.getGameProfile()));

        EventManager.get().fireEvent(new WhitelistModifyEvent(this, uuid, WhitelistModifyEvent.Action.ADD));
    }

    @Override
    public void remove(UUID uuid) {
        ServerPlayer player = MeepFabric.getServer().getPlayerList().getPlayer(uuid);
        if (player == null) {
            return;
        }

        MeepFabric.getServer().getPlayerList().getWhiteList().remove(new UserWhiteListEntry(player.getGameProfile()));

        EventManager.get().fireEvent(new WhitelistModifyEvent(this, uuid, WhitelistModifyEvent.Action.REMOVE));
    }

    @Override
    public boolean inList(UUID uuid) {
        ServerPlayer player = MeepFabric.getServer().getPlayerList().getPlayer(uuid);
        if (player == null) {
            return false;
        }

        EventManager.get().fireEvent(new WhitelistModifyEvent(this, uuid, WhitelistModifyEvent.Action.CHECK));

        return MeepFabric.getServer().getPlayerList().getWhiteList().isWhiteListed(player.getGameProfile());
    }

    @Override
    public void enable() {
        MeepFabric.getServer().setEnforceWhitelist(true);
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, null, WhitelistModifyEvent.Action.ENABLE));
    }

    @Override
    public void disable() {
        MeepFabric.getServer().setEnforceWhitelist(false);
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, null, WhitelistModifyEvent.Action.DISABLE));
    }

    @Override
    public void kickAllUnpermittedPlayers() {
        for (ServerPlayer player : MeepFabric.getServer().getPlayerList().getPlayers()) {
            if (!MeepFabric.getServer().getPlayerList().isWhiteListed(player.getGameProfile())) {
                player.connection.disconnect(Component.translatable("multiplayer.disconnect.not_whitelisted"));
            }
        }
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, null, WhitelistModifyEvent.Action.KICK_UNPERMITTED));
    }
}
