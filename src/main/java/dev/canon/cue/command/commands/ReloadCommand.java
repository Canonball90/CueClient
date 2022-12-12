package dev.canon.cue.command.commands;

import dev.canon.cue.Cue;
import dev.canon.cue.command.Command;
import dev.canon.cue.command.CommandInfo;
import dev.canon.cue.utils.client.ChatUtil;

@CommandInfo(name = "reload", aliases = {"reloadConfig"}, descriptions = "reload configuration file", usage = "reload")
public class ReloadCommand extends Command {

    @Override
    public void execute(String[] args) {
        Cue.configManager.loadAll();
        ChatUtil.sendMessage("&aSuccess reload all configurations");
    }
}
