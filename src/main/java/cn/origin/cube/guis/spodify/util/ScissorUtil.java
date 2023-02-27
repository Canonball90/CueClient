package cn.origin.cube.guis.spodify.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import static org.lwjgl.opengl.GL11.glScissor;

/**
 * @author cedo
 * Aug 22, 2021
 */

public class ScissorUtil {

    public static void scissor(double x, double y, double width, double height) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        final double scale = sr.getScaleFactor();
        y = sr.getScaledHeight() - y;
        x *= scale;
        y *= scale;
        width *= scale;
        height *= scale;
        glScissor((int) x, (int) (y - height), (int) width, (int) height);
    }

}
