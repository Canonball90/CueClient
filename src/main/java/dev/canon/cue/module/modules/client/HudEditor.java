package dev.canon.cue.module.modules.client;

import dev.canon.cue.Cue;
import dev.canon.cue.guis.HudEditorScreen;
import dev.canon.cue.managers.ConfigManager;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

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
            Module.mc.displayGuiScreen(Cue.hudEditor);
        }
    }

    public void onDisable() {
        if (!this.fullNullCheck() && Module.mc.currentScreen instanceof HudEditorScreen) {
            Module.mc.displayGuiScreen(null);
            ConfigManager configManager = Cue.configManager;
            configManager.saveAll();
        }

    }
}
