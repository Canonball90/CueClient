package cn.origin.cube.module.huds;

import cn.origin.cube.Cube;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.HudModule;
import cn.origin.cube.core.module.interfaces.HudModuleInfo;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@HudModuleInfo(name = "SessionInfo", descriptions = "Show all enable module", category = Category.HUD, y = 100, x = 100)
public class SessionInfo extends HudModule {

    @Override
    public void onRender2D() {
        final int[] counter = {1};
        String server;
        if (mc.isSingleplayer()) {
            server = "localhost";
        } else {
            server = mc.getCurrentServerData().serverIP.toLowerCase();
        }
        GL11.glPushMatrix();
        GL11.glTranslated(this.x, (float) this.y, 0);
        final String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        final double prevZ = SessionInfo.mc.player.posZ - SessionInfo.mc.player.prevPosZ;
        final double prevX = SessionInfo.mc.player.posX - SessionInfo.mc.player.prevPosX;
        final double lastDist = Math.sqrt(prevX * prevX + prevZ * prevZ);
        final double currSpeed = lastDist * 15.3571428571;
        final String speed = String.format("%.2f bps", currSpeed);
        double posX = 567.0;
        double posY = -435.0;
        ScaledResolution sr = new ScaledResolution(mc);
        final float scaledWidth = sr.getScaledWidth();
        float x = (float) ((double) (scaledWidth / 2.0F) - posX);
        float y = (float) ((double) (scaledWidth / 2.0F) + posY);
        Gui.drawRect((int) (x + 90.0F), (int) ((double) y - 1.5), (int) (x + 200.5F), (int) (y + 60.0F), (new Color(0, 0, 0, 190)).getRGB());
        Gui.drawRect((int) (x + 90.0F), (int) ((double) y - 1.7), (int) (x + 200.5F), (int) (y + 0.3F), new Color(94, 0, 255).getRGB());
        double posX2 = 567.0;
        double posY2 = -422.0;
        final float scaledWidth2 = sr.getScaledWidth();
        float x2 = (float) ((double) (scaledWidth / 2.0F) - posX2);
        float y2 = (float) ((double) (scaledWidth / 2.0F) + posY2);
        Gui.drawRect((int) (x2 + 90.0F), (int) ((double) y2 - 0.7), (int) (x2 + 200.5F), (int) (y2 + 0.1F), new Color(67, 67, 67).getRGB());
        Cube.fontManager.CustomFont.drawString("         Session Info         ", 5, 49, new Color(255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Server: " + server, 6, 65, new Color(255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Name: " + SessionInfo.mc.player.getName(), 6, 75, new Color(255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Time: " + time, 6, 85, new Color(255, 255, 255).getRGB());
        Cube.fontManager.CustomFont.drawString("Speed: " + speed, 6, 95, new Color(255, 255, 255).getRGB());
        GL11.glPopMatrix();
        this.width = (int) ((float) Cube.fontManager.CustomFont.getStringWidth("CueClient"));
        this.height = (int) ((float) Cube.fontManager.CustomFont.getHeight());
        super.onRender2D();
    }
}
