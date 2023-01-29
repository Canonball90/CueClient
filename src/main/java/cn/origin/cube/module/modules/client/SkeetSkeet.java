package cn.origin.cube.module.modules.client;

import cn.origin.cube.Cube;
import cn.origin.cube.core.managers.ConfigManager;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.guis.csgoGui.gui.SkeetGUI;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;

@Constant(constant = false)
@ModuleInfo(name = "SkeetGui", descriptions = "", category = Category.CLIENT)
public class SkeetSkeet extends Module {

    public void onEnable() {
        if (!this.fullNullCheck() && !(Module.mc.currentScreen instanceof SkeetGUI)) {
            Module.mc.displayGuiScreen(new SkeetGUI());
        }
        disable();
    }

    public void onDisable() {
        if (!this.fullNullCheck() && Module.mc.currentScreen instanceof SkeetGUI) {
            Module.mc.displayGuiScreen(null);
            ConfigManager configManager = Cube.configManager;
            configManager.saveAll();
        }
    }
}
