/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.paper.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kore.meep.api.event.EventManager;
import net.kore.meep.api.event.player.PlayerChatEvent;
import net.kore.meep.api.event.player.PlayerSwitchWorldEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import static net.kore.meep.paper.utils.BukkitToMeep.*;

public class PaperEventListener implements Listener {
    @EventHandler
    public void joinEvent(PlayerJoinEvent e) {
        net.kore.meep.api.event.player.PlayerJoinEvent event = new net.kore.meep.api.event.player.PlayerJoinEvent(player(e.getPlayer()), e.joinMessage());
        EventManager.get().fireEvent(event);
        e.joinMessage(event.getJoinMessage());
    }

    @EventHandler
    public void chatEvent(AsyncChatEvent e) {
        PlayerChatEvent event = new PlayerChatEvent(player(e.getPlayer()), e.message(), e.viewers());
        if (EventManager.get().fireEvent(event)) {
            e.message(event.getMessage());
            e.viewers().clear();
            event.getAudience().forEach((a) -> e.viewers().add(a));
        } else e.setCancelled(true);
    }

    @EventHandler
    public void switchWorld(PlayerChangedWorldEvent e) {
        PlayerSwitchWorldEvent event = new PlayerSwitchWorldEvent(player(e.getPlayer()), world(e.getFrom()), world(e.getPlayer().getWorld()));
        EventManager.get().fireEvent(event);
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        net.kore.meep.api.event.block.BlockBreakEvent event = new net.kore.meep.api.event.block.BlockBreakEvent(block(e.getBlock()), player(e.getPlayer()));
        if (!EventManager.get().fireEvent(event)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e) {
        net.kore.meep.api.event.block.BlockPlaceEvent event = new net.kore.meep.api.event.block.BlockPlaceEvent(block(e.getBlock()), player(e.getPlayer()));
        if (!EventManager.get().fireEvent(event)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void worldLoad(WorldLoadEvent e) {
        net.kore.meep.api.event.world.WorldLoadEvent event = new net.kore.meep.api.event.world.WorldLoadEvent(world(e.getWorld()));
        EventManager.get().fireEvent(event);
    }

    @EventHandler
    public void worldUnload(WorldUnloadEvent e) {
        net.kore.meep.api.event.world.WorldUnloadEvent event = new net.kore.meep.api.event.world.WorldUnloadEvent(world(e.getWorld()));
        EventManager.get().fireEvent(event);
    }
}
