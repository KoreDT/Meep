package net.kore.meep.fabric.command;

import net.kore.meep.api.command.Command;
import net.kore.meep.api.command.CommandManager;

import java.util.ArrayList;
import java.util.List;

public class FabricCommandManager implements CommandManager {
    public List<Command> commands = new ArrayList<>();

    @Override
    public void registerCommand(Command command) {
        commands.add(command);
    }
}
