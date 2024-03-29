package cn.origin.cube.module.modules.client;

import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.guis.otheruis.console.GuiConsole;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import org.lwjgl.input.Keyboard;

@Constant(constant = false)
@ModuleInfo(name = "Console", descriptions = "open Console screen", category = Category.CLIENT, defaultKeyBind = Keyboard.KEY_P)
public class Console extends Module {

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new GuiConsole());
        disable();
        super.onEnable();
    }
}
