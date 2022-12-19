package cn.origin.cube.module.modules.client;

import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.ModeSetting;

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
