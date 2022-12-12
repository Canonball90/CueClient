package dev.canon.cue.command.commands;

import dev.canon.cue.Cue;
import dev.canon.cue.command.Command;
import dev.canon.cue.command.CommandInfo;
import dev.canon.cue.utils.client.ChatUtil;

import java.util.Arrays;

@CommandInfo(name = "help", aliases = {"?", "h"}, descriptions = "Show command list", usage = "Help")
public class HelpCommand extends Command {
    @Override
    public void execute(String[] args) {
        ChatUtil.sendMessage("Commands list:");
        for (Command command : Cue.commandManager.getCommands()) {
            ChatUtil.sendColoredMessage("&bCommand: &6" + command.name + "&b " + command.descriptions + " &bUsage: " + command.usage + " &bAliases: " + Arrays.toString(command.aliases));
        }
    }
}
