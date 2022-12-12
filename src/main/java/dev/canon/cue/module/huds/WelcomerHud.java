package dev.canon.cue.module.huds;

import dev.canon.cue.Cue;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.HudModule;
import dev.canon.cue.module.HudModuleInfo;
import dev.canon.cue.module.modules.client.ClickGui;
import dev.canon.cue.settings.FloatSetting;
import org.lwjgl.opengl.GL11;

@HudModuleInfo(name = "Welcomer", x = 10, y = 500, descriptions = "Show hack name", category = Category.HUD)
public class WelcomerHud extends HudModule {

    public FloatSetting Scala = registerSetting("Size", 1.0f, 0.0f, 3.0f);

    @Override
    public void onRender2D() {
        GL11.glPushMatrix();
        GL11.glTranslated(this.x, this.y, 0);
        GL11.glScaled((double) this.Scala.getValue(), (double) this.Scala.getValue(), 0.0);
        Cue.fontManager.CustomFont.drawString("welcome to CueClient, " + mc.player.getName(), 0, 0, ClickGui.getCurrentColor().getRGB());
        GL11.glPopMatrix();
        this.width = (int) ((float) Cue.fontManager.CustomFont.getStringWidth("CueClient") * this.Scala.getValue());
        this.height = (int) ((float) Cue.fontManager.CustomFont.getHeight() * this.Scala.getValue());
    }

}
