package cn.origin.cube.module.modules.client;

import cn.origin.cube.Cube;
import cn.origin.cube.core.managers.ConfigManager;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.guis.HudEditorScreen;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import org.lwjgl.input.Keyboard;

@Constant(constant = false)
@ModuleInfo(
        name = "HudEditor",
        descriptions = "open Hud screen",
        category = Category.CLIENT,
        defaultKeyBind = Keyboard.KEY_GRAVE
)
public class HudEditor extends Module {

    public static HudEditor INSTANCE;

    public HudEditor() {
        INSTANCE = this;
    }

    public void onEnable() {
        if (!this.fullNullCheck() && !(Module.mc.currentScreen instanceof HudEditorScreen)) {
            Module.mc.displayGuiScreen(Cube.hudEditor);
        }
    }

    public void onDisable() {
        if (!this.fullNullCheck() && Module.mc.currentScreen instanceof HudEditorScreen) {
            Module.mc.displayGuiScreen(null);
            ConfigManager configManager = Cube.configManager;
            configManager.saveAll();
        }

    }
}
