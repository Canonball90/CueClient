package cn.origin.cube.module.modules.client;

import cn.origin.cube.Cube;
import cn.origin.cube.guis.ClickGuiScreen;
import cn.origin.cube.guis.csgoGui.gui.SkeetGUI;
import cn.origin.cube.managers.ConfigManager;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;

@ModuleInfo(name = "SkeetGui", descriptions = "", category = Category.CLIENT)
public class SkeetSkeet extends Module {

    public void onEnable() {
        if (!this.fullNullCheck() && !(Module.mc.currentScreen instanceof ClickGuiScreen)) {
            Module.mc.displayGuiScreen(new SkeetGUI());
        }
        disable();
    }

    public void onDisable() {
        if (!this.fullNullCheck() && Module.mc.currentScreen instanceof ClickGuiScreen) {
            Module.mc.displayGuiScreen(null);
            ConfigManager configManager = Cube.configManager;
            configManager.saveAll();
        }
    }
}
