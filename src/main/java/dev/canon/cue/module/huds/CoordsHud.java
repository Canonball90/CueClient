package dev.canon.cue.module.huds;

import dev.canon.cue.Cue;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.HudModule;
import dev.canon.cue.module.HudModuleInfo;
import dev.canon.cue.module.modules.client.ClickGui;
import dev.canon.cue.settings.FloatSetting;
import net.minecraft.world.DimensionType;
import org.lwjgl.opengl.GL11;

@HudModuleInfo(name = "Coords", x = 114, y = 114, descriptions = "Show hack name", category = Category.HUD)
public class CoordsHud extends HudModule {

    public FloatSetting Scala = registerSetting("Size", 1.0f, 0.0f, 3.0f);

    @Override
    public void onRender2D() {
        GL11.glPushMatrix();
        GL11.glTranslated(this.x, this.y, 0);
        GL11.glScaled((double) this.Scala.getValue(), (double) this.Scala.getValue(), 0.0);
        String coords = "";
        if (mc.player.world.provider.getDimensionType() == DimensionType.NETHER) {
            coords = String.format("X:%s Y:%s Z:%s | OW: [X:%s Y:%s Z:%s]",
                    (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ,
                    (int) mc.player.posX * 8, (int) mc.player.posY * 8, (int) mc.player.posZ * 8);
        } else {
            coords = String.format("X:%s Y:%s Z:%s | NETHER: [X:%s Y:%s Z:%s]",
                    (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ,
                    (int) mc.player.posX / 8, (int) mc.player.posY / 8, (int) mc.player.posZ / 8);
        }
        Cue.fontManager.CustomFont.drawString(coords, 0, 0, ClickGui.getCurrentColor().getRGB());
        GL11.glPopMatrix();
        this.width = (int) ((float) Cue.fontManager.CustomFont.getStringWidth("CueClient") * this.Scala.getValue());
        this.height = (int) ((float) Cue.fontManager.CustomFont.getHeight() * this.Scala.getValue());
    }
}
