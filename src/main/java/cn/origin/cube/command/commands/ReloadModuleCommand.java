package cn.origin.cube.command.commands;

import cn.origin.cube.Cube;
import cn.origin.cube.command.Command;
import cn.origin.cube.core.module.interfaces.CommandInfo;
import cn.origin.cube.utils.client.ChatUtil;

@CommandInfo(name = "Mreload", aliases = {"reloadM"}, descriptions = "reload configuration Module", usage = "Mreload <module>")
public class ReloadModuleCommand extends Command {

    //Todo
    @Override
    public void execute(String[] args) {
        Cube.configManager.loadAll();
        ChatUtil.sendMessage("&aSuccess reload all configurations");
    }
}
