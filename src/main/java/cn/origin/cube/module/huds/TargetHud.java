package cn.origin.cube.module.huds;

import cn.origin.cube.Cube;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.HudModule;
import cn.origin.cube.core.module.interfaces.HudModuleInfo;
import cn.origin.cube.module.modules.combat.AutoCrystal.AutoCrystal;
import cn.origin.cube.core.settings.IntegerSetting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;

@HudModuleInfo(name = "renderEnt", descriptions = "Shows your mother's info.", category = Category.HUD, y = 200, x = 100)
public class TargetHud extends HudModule {
    IntegerSetting h = registerSetting("Height", 100, 10, 500);
    IntegerSetting w = registerSetting("Width", 200, 10, 500);
    float xpos = x;
    float ypos = y;
    EntityPlayer renderEnt;
    private int widthl = w.getValue();
    private int heightl = h.getValue();

    static double healthBarrenderEnt = 0, healthBar = 0;
    float lastHealth = 0;
    
    
    @Override
    public void onRender2D() {
        if(AutoCrystal.INSTANCE.isEnabled()){
            renderEnt = (EntityPlayer) AutoCrystal.INSTANCE.renderEnt;
        }else{
            renderEnt = null;
        }
        DecimalFormat dec = new DecimalFormat("#");
        if(renderEnt != null) {
            healthBarrenderEnt = xpos / 2 - 41 + (((140) / (renderEnt.getMaxHealth())) * (renderEnt.getHealth()));
            double HealthBarSpeed = 5;

            if (healthBar > healthBarrenderEnt) {
                healthBar = ((healthBar) - ((healthBar - healthBarrenderEnt) / HealthBarSpeed));
            } else if (healthBar < healthBarrenderEnt) {
                healthBar = ((healthBar) + ((healthBarrenderEnt - healthBar) / HealthBarSpeed));
            }
            int color = (renderEnt.getHealth() / renderEnt.getMaxHealth() > 0.66f) ? 0xff00ff00 : (renderEnt.getHealth() / renderEnt.getMaxHealth() > 0.33f) ? 0xffff9900 : 0xffff0000;
            GL11.glPushMatrix();
            GL11.glTranslated(this.x, (float) this.y, 0);
            color = 0xff00ff00;
            float[] hsb = Color.RGBtoHSB(((int) 255), ((int) 255), ((int) 255), null);
            float hue = hsb[0];
            float saturation = hsb[1];
            color = Color.HSBtoRGB(hue, saturation, 1);

            float hue1 = System.currentTimeMillis() % (int) ((100.5f - 50) * 1000) / (float) ((100.5f - 50) * 1000);
            color = Color.HSBtoRGB(hue1, 0.65f, 1);

            Gui.drawRect((int) (xpos / 2 - 110), (int) ypos / 2 + 100, (int) xpos / 2 + 110, (int) ypos / 2 + 170, 0xff36393f);
            Gui.drawRect((int) xpos / 2 - 41, (int) ypos / 2 + 100 + 54, (int) xpos / 2 + 100, (int) ypos / 2 + 96 + 45, 0xff202225);
            Gui.drawRect((int) xpos / 2 - 41, (int) ypos / 2 + 100 + 54, (int) healthBar, (int) ypos / 2 + 96 + 45, color);
            GlStateManager.color(1, 1, 1);
            GuiInventory.drawEntityOnScreen((int) xpos / 2 - 75, (int) ypos / 2 + 165, 25, 1f, 1f, renderEnt);
            Cube.fontManager.CustomFont.drawString(renderEnt.getName(), xpos / 2 - 40, ypos / 2 + 110, -1);
            Cube.fontManager.CustomFont.drawString("HP: ", xpos / 2 - 40, ypos / 2 + 125, -1);
            Cube.fontManager.CustomFont.drawString("搂c鉂�: 搂f" + dec.format(renderEnt.getHealth()), xpos / 2 - 40 + Cube.fontManager.CustomFont.getStringWidth("HP: "), ypos / 2 + 125, color);
            GL11.glPopMatrix();
        }
        this.width = (int) xpos / 2 + 110;
        this.height = ypos / 2 + 170;
    }

    public void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8, 8, 8, 8, 37, 37, 64, 64);
    }
}
