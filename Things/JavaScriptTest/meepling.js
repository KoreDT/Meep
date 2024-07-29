JImport('net.kyori.adventure.text.Component'); // Automatically gives the global value depending on it's class name (so this one can be used via `Component`)
JImport('net.kyori.adventure.text.format.NamedTextColor');

JImport('net.kore.meep.api.event.EventManager');
JImport('net.kore.meep.api.event.player.PlayerJoinEvent');
JImport('net.kore.meep.api.event.lifecycle.EnableEvent');
JImport('net.kore.meep.api.event.lifecycle.DisableEvent');

const onEnable = require("./onEnable.js");
const onDisable = require("./onDisable.js");

EventManager.get().registerListener(PlayerJoinEvent.class, (e) => {
    e.getPlayer().sendMessage(Component.text("Welcome!").color(NamedTextColor.GOLD));
});

EventManager.get().registerListener(EnableEvent.class, onEnable);
EventManager.get().registerListener(DisableEvent.class, onDisable);