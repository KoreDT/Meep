/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper;

import net.kore.meep.api.WhitelistManager;
import net.kore.meep.api.event.EventManager;
import net.kore.meep.api.event.server.WhitelistModifyEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PaperWhitelistManager implements WhitelistManager {
    @Override
    public void add(UUID uuid) {
        Bukkit.getOfflinePlayer(uuid).setWhitelisted(true);
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, uuid, WhitelistModifyEvent.Action.ADD));
    }

    @Override
    public void remove(UUID uuid) {
        Bukkit.getOfflinePlayer(uuid).setWhitelisted(false);
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, uuid, WhitelistModifyEvent.Action.REMOVE));
    }

    @Override
    public boolean inList(UUID uuid) {
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, uuid, WhitelistModifyEvent.Action.CHECK));
        return Bukkit.getOfflinePlayer(uuid).isWhitelisted();
    }

    @Override
    public void enable() {
        Bukkit.setWhitelistEnforced(true);
        Bukkit.setWhitelist(true);
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, null, WhitelistModifyEvent.Action.ENABLE));
    }

    @Override
    public void disable() {
        Bukkit.setWhitelistEnforced(false);
        Bukkit.setWhitelist(false);
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, null, WhitelistModifyEvent.Action.DISABLE));
    }

    @Override
    public void kickAllUnpermittedPlayers() {
        if (Bukkit.isWhitelistEnforced()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.isWhitelisted()) {
                    player.kick(Component.text("You are not whitelisted on this server."));
                }
            }
        }
        EventManager.get().fireEvent(new WhitelistModifyEvent(this, null, WhitelistModifyEvent.Action.KICK_UNPERMITTED));
    }
}
