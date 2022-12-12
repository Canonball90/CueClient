package dev.canon.cue.module.modules.client;

import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.settings.ModeSetting;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import org.lwjgl.input.Keyboard;

@ModuleInfo(name = "MainMenuShader", descriptions = "open click gui screen", category = Category.CLIENT)
public class MainMenuShader extends Module {

    public ModeSetting<Shades> shaders = registerSetting("Shader", Shades.Flow);

    public enum Shades {
        Flow, Smoke, Red, Ranbow, Aqua
    }

    public static MainMenuShader INSTANCE;

    public MainMenuShader() {
        INSTANCE = this;
    }

}
