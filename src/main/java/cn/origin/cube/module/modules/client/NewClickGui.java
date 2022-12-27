package cn.origin.cube.module.modules.client;

import cn.origin.cube.Cube;
import cn.origin.cube.core.managers.ConfigManager;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.guis.ClickGuiScreen;
import cn.origin.cube.guis.newGui.panel.NewGui;
import cn.origin.cube.module.interfaces.ModuleInfo;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "NewClickGui", descriptions = "open click gui screen", category = Category.CLIENT)
public final class NewClickGui extends Module {
    public BooleanSetting rainbow = registerSetting("Rainbow", false);
    public BooleanSetting gay = registerSetting("Gay", false);
    public IntegerSetting red = registerSetting("Red", 25, 0, 255).booleanDisVisible(rainbow);
    public IntegerSetting green = registerSetting("Green", 115, 0, 255).booleanDisVisible(rainbow);
    public IntegerSetting blue = registerSetting("Blue", 255, 0, 255).booleanDisVisible(rainbow);
    public FloatSetting speed = registerSetting("RainbowSpeed", 1.0f, 0.1f, 10.0f).booleanVisible(rainbow);
    public FloatSetting saturation = registerSetting("Saturation", 0.65f, 0.0f, 1.0f).booleanVisible(rainbow);
    public FloatSetting brightness = registerSetting("Brightness", 1.0f, 0.0f, 1.0f).booleanVisible(rainbow);
    public BooleanSetting outline = registerSetting("Outline", false);
    public BooleanSetting override = registerSetting("Override", false);
    public FloatSetting animationLength = registerSetting("AnimLength", 300F, 100F, 1000F);
    public BooleanSetting background = registerSetting("BackGround", false);
    public BooleanSetting gradient = registerSetting("Gradient", false).booleanVisible(background);
    public BooleanSetting particles = registerSetting("Particles", false).booleanVisible(background);//partLength
    public IntegerSetting partLength = registerSetting("Length", 150, 0, 300).booleanVisible(background).booleanVisible(particles);

    public static NewClickGui INSTANCE;

    public NewClickGui() {
        INSTANCE = this;
    }

    public void onEnable() {
        if (!this.fullNullCheck() && !(Module.mc.currentScreen instanceof NewGui)) {
            Module.mc.displayGuiScreen(new NewGui());
        }

    }

    public void onDisable() {
        if (!this.fullNullCheck() && Module.mc.currentScreen instanceof NewGui) {
            Module.mc.displayGuiScreen(null);
            ConfigManager configManager = Cube.configManager;
            configManager.saveAll();
        }
    }

    public static Color getRainbow() {
        float hue = (float) (System.currentTimeMillis() % 11520L) / 11520.0f * INSTANCE.speed.getValue();
        return new Color(Color.HSBtoRGB(hue, INSTANCE.saturation.getValue(), INSTANCE.brightness.getValue()));
    }

    public static Color getCurrentColor() {
        return INSTANCE.rainbow.getValue() ? getRainbow() : new Color(INSTANCE.red.getValue(), INSTANCE.green.getValue(), INSTANCE.blue.getValue());
    }
}
