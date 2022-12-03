package cn.origin.cube.module.huds;

import cn.origin.cube.Cube;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.HudModule;
import cn.origin.cube.module.HudModuleInfo;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.settings.BooleanSetting;
import cn.origin.cube.settings.FloatSetting;
import cn.origin.cube.settings.ModeSetting;
import org.lwjgl.opengl.GL11;

@HudModuleInfo(name = "WaterMark", x = 114, y = 114, descriptions = "Show hack name", category = Category.HUD)
public class WaterMark extends HudModule {
    public FloatSetting Scala = registerSetting("Size", 1.0f, 0.0f, 10.0f);
    public ModeSetting<nfd> mode = registerSetting("Mode", nfd.Word);
    public BooleanSetting version = registerSetting("Version", false);

    @Override
    public void onRender2D() {
        GL11.glPushMatrix();
        GL11.glTranslated(this.x, (float) this.y, 0);
        GL11.glScaled((double) this.Scala.getValue(), (double) this.Scala.getValue(), 0.0);
        if(mode.getValue().equals(nfd.Word)) {
            Cube.fontManager.CustomFont.drawString("CueClient", 0, 0, ClickGui.getCurrentColor().getRGB());
        }
        if(mode.getValue().equals(nfd.Logo)) {
            Cube.fontManager.IconFont.drawString("t", 0, 0, ClickGui.getCurrentColor().getRGB());
        }
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(this.x, (float) this.y, 0);
        GL11.glScaled((double) 1, (double) 1, 0.0);
        if(version.getValue()) {
            Cube.fontManager.CustomFont.drawString("ver:" + Cube.MOD_VERSION, 0 + width, 0 + height - 1, ClickGui.getCurrentColor().getRGB());
        }
        GL11.glPopMatrix();
        this.width = (int) ((float) Cube.fontManager.CustomFont.getStringWidth("CueClient") * this.Scala.getValue());
        this.height = (int) ((float) Cube.fontManager.CustomFont.getHeight() * this.Scala.getValue());
    }

    public enum nfd{
        Logo,Word
    }
}
