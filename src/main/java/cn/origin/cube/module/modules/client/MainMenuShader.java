package cn.origin.cube.module.modules.client;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.ModeSetting;

@Constant(constant = false)
@ModuleInfo(name = "MainMenuShader", descriptions = "open click gui screen", category = Category.CLIENT)
public class MainMenuShader extends Module {

    public ModeSetting<Shades> shaders = registerSetting("Shader", Shades.Flow);

    public enum Shades{
        Flow,Smoke,Red,Ranbow,Aqua
    }

    public static MainMenuShader INSTANCE;

    public MainMenuShader() {
        INSTANCE = this;
    }

}
