package dev.canon.cue.module.modules.client;

import dev.canon.cue.Cue;
import dev.canon.cue.guis.ClickGuiScreen;
import dev.canon.cue.managers.ConfigManager;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.settings.BooleanSetting;
import dev.canon.cue.settings.FloatSetting;
import dev.canon.cue.settings.IntegerSetting;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@ModuleInfo(name = "ClickGui", descriptions = "open click gui screen", category = Category.CLIENT, defaultKeyBind = Keyboard.KEY_RSHIFT)
public final class ClickGui extends Module {
    public BooleanSetting rainbow = registerSetting("Rainbow", false);
    public BooleanSetting gay = registerSetting("Gay", false);
    public IntegerSetting red = registerSetting("Red", 25, 0, 255).booleanDisVisible(rainbow);
    public IntegerSetting green = registerSetting("Green", 115, 0, 255).booleanDisVisible(rainbow);
    public IntegerSetting blue = registerSetting("Blue", 255, 0, 255).booleanDisVisible(rainbow);
    public FloatSetting speed = registerSetting("RainbowSpeed", 1.0f, 0.1f, 10.0f).booleanVisible(rainbow);
    public FloatSetting saturation = registerSetting("Saturation", 0.65f, 0.0f, 1.0f).booleanVisible(rainbow);
    public FloatSetting brightness = registerSetting("Brightness", 1.0f, 0.0f, 1.0f).booleanVisible(rainbow);
    public BooleanSetting outline = registerSetting("Outline", false);
    public BooleanSetting background = registerSetting("BackGround", false);
    public BooleanSetting gradient = registerSetting("Gradient", false).booleanVisible(background);
    public BooleanSetting particles = registerSetting("Particles", false).booleanVisible(background);//partLength
    public IntegerSetting partLength = registerSetting("Length", 150, 0, 300).booleanVisible(background).booleanVisible(particles);

    public static ClickGui INSTANCE;

    public ClickGui() {
        INSTANCE = this;
    }

    public void onEnable() {
        if (!this.fullNullCheck() && !(Module.mc.currentScreen instanceof ClickGuiScreen)) {
            Module.mc.displayGuiScreen(Cue.clickGui);
        }

    }

    public void onDisable() {
        if (!this.fullNullCheck() && Module.mc.currentScreen instanceof ClickGuiScreen) {
            Module.mc.displayGuiScreen(null);
            ConfigManager configManager = Cue.configManager;
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