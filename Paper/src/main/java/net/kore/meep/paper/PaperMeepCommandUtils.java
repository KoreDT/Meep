package net.kore.meep.paper;

import net.kore.meep.api.command.CommandSender;
import net.kore.meep.api.command.MeepCommandUtils;

public class PaperMeepCommandUtils implements MeepCommandUtils {
    @Override
    public int executeCommandUsingMeep(CommandSender sender, String command) {
        try {
            return MeepPaper.getPlugin().DISPATCHER.execute(command, sender);
        } catch (Exception e) {
            return 0;
        }
    }
}
