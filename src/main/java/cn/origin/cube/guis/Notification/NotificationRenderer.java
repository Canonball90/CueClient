package cn.origin.cube.guis.Notification;

import cn.origin.cube.Cube;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.utils.render.Render2DUtil;
import cn.origin.cube.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class NotificationRenderer {

    public static final int HEIGHT = 40;
    static Minecraft mc = Minecraft.getMinecraft();

    public static void renderNotification(String title, String msg, float x, float y, int more) {
        int width = mc.fontRenderer.getStringWidth(msg) + 70;
        Render2DUtil.drawRect1(x, y, x + width, y + HEIGHT, new Color(0, 0, 0, 140).getRGB());
        Render2DUtil.drawRect1(x, y, x + 2, y + HEIGHT, Colors.getGlobalColor().getRGB());
        Cube.fontManager.CustomFont.drawString(title, (int) ((float) x + 15), (int) (y + 9), new Color(255,255,255,255).getRGB());
        Cube.fontManager.CustomFont.drawString(msg, (int) (x + 15), (int) (y + HEIGHT - mc.fontRenderer.FONT_HEIGHT - 8),  new Color(255,255,255,255).getRGB());
        if (more > 0) {
            Cube.fontManager.CustomFont.drawString(more + " more", (int) (x + width - mc.fontRenderer.getStringWidth(more + " more") - 7), (int) (y + 10),  new Color(255,255,255,255).getRGB());
        }
    }

    public static float getNotificationWidth(String msg) {
        return Cube.fontManager.CustomFont.getStringWidth(msg) + 70;
    }
}
