package dev.canon.cue.module.modules.client;

import dev.canon.cue.guis.console.GuiConsole;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "Console", descriptions = "open Console screen", category = Category.CLIENT, defaultKeyBind = Keyboard.KEY_P)
public class Console extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new GuiConsole());
        disable();
        super.onEnable();
    }
}
