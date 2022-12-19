package cn.origin.cube.module.huds;

import cn.origin.cube.Cube;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.HudModule;
import cn.origin.cube.module.interfaces.HudModuleInfo;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.module.modules.combat.AutoCrystal.AutoCrystal;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@HudModuleInfo(name = "Target", descriptions = "Shows your mother's info.", category = Category.HUD, y = 200, x = 100)
public class TargetHud extends HudModule {
    IntegerSetting h = registerSetting("Height", 100, 10, 500);
    IntegerSetting w = registerSetting("Width", 200, 10, 500);
    float xpos = x;
    float ypos = y;
    EntityPlayer renderEnt;
    private int widthl = w.getValue();
    private int heightl = h.getValue();


    @Override
    public void onRender2D() {
        if(AutoCrystal.INSTANCE.isEnabled()){
            renderEnt = (EntityPlayer) AutoCrystal.INSTANCE.renderEnt;
        }else{
            renderEnt = null;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(this.x, (float) this.y, 0);
        if ((mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)) {
            Render2DUtil.drawBorderedRect(xpos, ypos, xpos + widthl, ypos + heightl, 1, new Color(35, 35, 35, 150).getRGB(), ClickGui.getCurrentColor().getRGB());
            if(renderEnt != null) {
                drawHead((mc.getConnection()).getPlayerInfo(renderEnt.getUniqueID()).getLocationSkin(), (int) (xpos + 5), (int) (ypos + 10));
            }
            Cube.fontManager.CustomFont.drawString((renderEnt == null) ? "None" : renderEnt.getName(), xpos + 43, ypos + 10, ClickGui.getCurrentColor().getRGB(), true);
            Cube.fontManager.CustomFont.drawString((renderEnt == null) ? "None" : "" + renderEnt.getDistance(mc.player), xpos + 43, ypos + 20, ClickGui.getCurrentColor().getRGB(), true);
            if(renderEnt != null) {
                Render2DUtil.drawGradientHRect(xpos + 5, ypos + 55, xpos + 140, ypos + 67 - (renderEnt.getHealth()), new Color(255, 0, 0).getRGB(), new Color(0, 255, 0).getRGB());
            }
        }
        GL11.glPopMatrix();
        this.width = (int) ((float) Cube.fontManager.CustomFont.getStringWidth("CueClient"));
        this.height = (int) ((float) Cube.fontManager.CustomFont.getHeight());
    }

    public void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8, 8, 8, 8, 37, 37, 64, 64);
    }
}
