package cn.origin.cube.command.commands;

import cn.origin.cube.Cube;
import cn.origin.cube.command.Command;
import cn.origin.cube.core.module.interfaces.CommandInfo;
import cn.origin.cube.utils.client.ChatUtil;

@CommandInfo(name = "save", aliases = {"saveCommand"}, descriptions = "save configuration file", usage = "save")
public class SaveCommand extends Command {
    @Override
    public void execute(String[] args) {
        Cube.configManager.saveAll();
        ChatUtil.sendMessage("Saved Config");
    }
}
