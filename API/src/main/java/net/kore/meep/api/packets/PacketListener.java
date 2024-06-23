package net.kore.meep.api.packets;

import net.kore.meep.api.entity.Player;

public interface PacketListener {
    void onPacket(Player player, String channel, byte[] data);
}
