package cn.origin.cube.utils.render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static cn.origin.cube.utils.render.Render3DUtil.mc;
import static cn.origin.cube.utils.render.RenderUtil.tessellator;
import static org.lwjgl.opengl.GL11.*;
import static sun.swing.SwingUtilities2.drawVLine;

public class Render2DUtil {

    public static void setColor(Color color) {
        GL11.glColor4d((double) color.getRed() / 255.0, (double) color.getGreen() / 255.0, (double) color.getBlue() / 255.0, (double) color.getAlpha() / 255.0);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float) (color >> 24 & 0xFF) / 255.0f;
        float red = (float) (color >> 16 & 0xFF) / 255.0f;
        float green = (float) (color >> 8 & 0xFF) / 255.0f;
        float blue = (float) (color & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y + h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x + w, y + h, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x + w, y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawLine(final float x, final float y, final float x1, final float y1, final float thickness, final int hex) {
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glLineWidth(thickness);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double)x, (double)y, 0.0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos((double)x1, (double)y1, 0.0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static void drawRect1(float x, float y, float x1, float y1, int color) {
        float alpha = (color >> 24 & 255) / 255.0F;
        float red = (color >> 16 & 255) / 255.0F;
        float green = (color >> 8 & 255) / 255.0F;
        float blue = (color & 255) / 255.0F;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y1, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y1, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x1, y, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutlineRect(double x, double y, double w, double h, float lineWidth, Color color) {
        drawLine(x, y, x + w, y, lineWidth, color);
        drawLine(x, y, x, y + h, lineWidth, color);
        drawLine(x, y + h, x + w, y + h, lineWidth, color);
        drawLine(x + w, y, x + w, y + h, lineWidth, color);
    }

    public static void drawLine(Double x1, Double y1, Double x2, Double y2, Float lineWidth) {
        glDisable(3553);
        glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        glDisable(3042);
        glEnable(3553);
    }

    public static void drawLine(double x1, double y1, double x2, double y2, float lineWidth, Color ColorStart) {
        glDisable(3553);
        glEnable(3042);
        GL11.glLineWidth(lineWidth);
        GL11.glShadeModel(7425);
        GL11.glBegin(2);
        setColor(ColorStart);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        glDisable(3042);
        glEnable(3553);
    }

    private static void fakeGuiRect(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    private static void enableGL2D() {
        glDisable(2929);
        glEnable(3042);
        glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }

    private static void disableGL2D() {
        glEnable(3553);
        glDisable(3042);
        glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }

    public static void drawBorderedRect(final double x, final double y, final double x1, final double y1, final double width, final int internalColor, final int borderColor) {
        enableGL2D();
        fakeGuiRect(x + width, y + width, x1 - width, y1 - width, internalColor);
        fakeGuiRect(x + width, y, x1 - width, y + width, borderColor);
        fakeGuiRect(x, y, x + width, y1, borderColor);
        fakeGuiRect(x1 - width, y, x1, y1, borderColor);
        fakeGuiRect(x + width, y1 - width, x1 - width, y1, borderColor);
        disableGL2D();
    }

    public static void drawGradientHRect(float x, float y, float x1, float y1, int topColor, int bottomColor) {
        float alpha = (topColor >> 24 & 255) / 255.0F;
        float red = (topColor >> 16 & 255) / 255.0F;
        float green = (topColor >> 8 & 255) / 255.0F;
        float blue = (topColor & 255) / 255.0F;

        float alpha2 = (bottomColor >> 24 & 255) / 255.0F;
        float red2 = (bottomColor >> 16 & 255) / 255.0F;
        float green2 = (bottomColor >> 8 & 255) / 255.0F;
        float blue2 = (bottomColor & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        final BufferBuilder builder = tessellator.getBuffer();

        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        builder.pos(x, y1, 0.0D).color(red, green, blue, alpha).endVertex();

        builder.pos(x1, y1, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        builder.pos(x1, y, 0.0D).color(red2, green2, blue2, alpha2).endVertex();
        tessellator.draw();

        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }


    public static void drawGradientHLine(float x, float y, float x1, int color1, int color2) {
        if (y < x) {
            float var5 = x;
            x = y;
            y = var5;
        }

        drawGradientHRect(x, x1, y + 1, x1 + 1, color1, color2);
    }

    public static void drawGradientBorderedRect(float x, float y, float x1, float y1, int insideC) {
        enableGL2D();
        x *= 2;
        x1 *= 2;
        y *= 2;
        y1 *= 2;
        glScalef(0.5F, 0.5F, 0.5F);
        drawVLine(x, y, y1 - 1, new Color(ColorUtil.getRainbow(5000, 0, 1)).getRGB());
        drawVLine(x1 - 1,y, y1, new Color(ColorUtil.getRainbow(5000, 1000, 1)).getRGB());
        drawGradientHLine(x, x1 - 1, y, new Color(ColorUtil.getRainbow(5000, 0, 1)).getRGB(), new Color(ColorUtil.getRainbow(5000, 1000, 1)).getRGB());
        drawGradientHLine(x, x1 - 2, y1 - 1, new Color(ColorUtil.getRainbow(5000, 0, 1)).getRGB(), new Color(ColorUtil.getRainbow(5000, 1000, 1)).getRGB());
        drawRect1(x + 1, y + 1, x1 - 1, y1 - 1, insideC);
        glScalef(2.0F, 2.0F, 2.0F);
        disableGL2D();
    }

    public static void drawVLine(float x, float y, float x1, int y1) {
        if (x1 < y) {
            float var5 = y;
            y = x1;
            x1 = var5;
        }

        drawRect1(x, y + 1, x + 1, x1, y1);
    }

    public static void drawVGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, top, 0.0).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawPolygonPart(final double x, final double y, final int radius, final int part, final int color, final int endcolor) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float alpha2 = (endcolor >> 24 & 0xFF) / 255.0f;
        final float red2 = (endcolor >> 16 & 0xFF) / 255.0f;
        final float green2 = (endcolor >> 8 & 0xFF) / 255.0f;
        final float blue2 = (endcolor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
        for (int i = part * 90; i <= part * 90 + 90; ++i) {
            final double angle = 6.283185307179586 * i / 360.0 + Math.toRadians(180.0);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).color(red2, green2, blue2, alpha2).endVertex();
        }
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawGlow(final double x, final double y, final double x1, final double y1, final int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        drawVGradientRect((float)(int)x, (float)(int)y, (float)(int)x1, (float)(int)(y + (y1 - y) / 2.0), ColorUtil.injectAlpha(new Color(color), 0).getRGB(), color);
        drawVGradientRect((float)(int)x, (float)(int)(y + (y1 - y) / 2.0), (float)(int)x1, (float)(int)y1, color, ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        final int radius = (int)((y1 - y) / 2.0);
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 0, color, ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 1, color, ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 2, color, ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 3, color, ColorUtil.injectAlpha(new Color(color), 0).getRGB());
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void rect(double x, double y, double width, double height, Color color) {
        GL11.glEnable((int) 3042);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glDisable((int) 3553);
        GL11.glDisable((int) 2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        if (color != null) {
            color(color);
        }
        GL11.glBegin((int) 6);
        GL11.glVertex2d((double) x, (double) y);
        GL11.glVertex2d((double) (x + width), (double) y);
        GL11.glVertex2d((double) (x + width), (double) (y + height));
        GL11.glVertex2d((double) x, (double) (y + height));
        GL11.glEnd();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable((int) 2884);
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        color(Color.white);
    }

    public static void drawImage(int x, int y, int w, int h, int color, String location) {
        if (color == 0) {
            GlStateManager.color((float) 1.0f, (float) 1.0f, (float) 1.0f);
        } else {
            Render2DUtil.color(new Color(color));
        }
        ResourceLocation resourceLocation = new ResourceLocation(location);
        mc.getTextureManager().bindTexture(resourceLocation);
        GlStateManager.enableBlend();
        GL11.glTexParameteri((int) 3553, (int) 10240, (int) 9729);
        Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, (float) 0.0f, (float) 0.0f, (int) w, (int) h, (float) w, (float) h);
        GlStateManager.disableBlend();
    }

    public static void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        GL11.glColor4d((double) ((float) color.getRed() / 255.0f), (double) ((float) color.getGreen() / 255.0f), (double) ((float) color.getBlue() / 255.0f), (double) ((float) color.getAlpha() / 255.0f));
    }

    public static boolean isHovered(int X, int Y, int W, int H, int mX, int mY) {
        return mX >= X && mX <= X + W && mY >= Y && mY <= Y + H;
    }

    public static void roundedRect(double x, double y, double width, double height, double edgeRadius, Color color) {
        double angle;
        double i;
        double halfRadius = edgeRadius / 2.0;
        width -= halfRadius;
        height -= halfRadius;
        float sideLength = (float) edgeRadius;
        sideLength /= 2.0f;
        GL11.glEnable((int) 3042);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glDisable((int) 3553);
        GL11.glDisable((int) 2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        if (color != null) {
            color(color);
        }
        GL11.glBegin((int) 6);
        for (i = 180.0; i <= 270.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            GL11.glVertex2d((double) (x + (double) sideLength * Math.cos(angle) + (double) sideLength), (double) (y + (double) sideLength * Math.sin(angle) + (double) sideLength));
        }
        GL11.glVertex2d((double) (x + (double) sideLength), (double) (y + (double) sideLength));
        GL11.glEnd();
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable((int) 2884);
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        color(Color.white);
        sideLength = (float) edgeRadius;
        sideLength /= 2.0f;
        GL11.glEnable((int) 3042);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glDisable((int) 3553);
        GL11.glDisable((int) 2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        if (color != null) {
            color(color);
        }
        GL11.glEnable((int) 2848);
        GL11.glBegin((int) 6);
        for (i = 0.0; i <= 90.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            GL11.glVertex2d((double) (x + width + (double) sideLength * Math.cos(angle)), (double) (y + height + (double) sideLength * Math.sin(angle)));
        }
        GL11.glVertex2d((double) (x + width), (double) (y + height));
        GL11.glEnd();
        GL11.glDisable((int) 2848);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable((int) 2884);
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        color(Color.white);
        sideLength = (float) edgeRadius;
        sideLength /= 2.0f;
        GL11.glEnable((int) 3042);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glDisable((int) 3553);
        GL11.glDisable((int) 2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        if (color != null) {
            color(color);
        }
        GL11.glEnable((int) 2848);
        GL11.glBegin((int) 6);
        for (i = 270.0; i <= 360.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            GL11.glVertex2d((double) (x + width + (double) sideLength * Math.cos(angle)), (double) (y + (double) sideLength * Math.sin(angle) + (double) sideLength));
        }
        GL11.glVertex2d((double) (x + width), (double) (y + (double) sideLength));
        GL11.glEnd();
        GL11.glDisable((int) 2848);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable((int) 2884);
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        color(Color.white);
        sideLength = (float) edgeRadius;
        sideLength /= 2.0f;
        GL11.glEnable((int) 3042);
        GL11.glBlendFunc((int) 770, (int) 771);
        GL11.glDisable((int) 3553);
        GL11.glDisable((int) 2884);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        if (color != null) {
            color(color);
        }
        GL11.glEnable((int) 2848);
        GL11.glBegin((int) 6);
        for (i = 90.0; i <= 180.0; i += 1.0) {
            angle = i * (Math.PI * 2) / 360.0;
            GL11.glVertex2d((double) (x + (double) sideLength * Math.cos(angle) + (double) sideLength), (double) (y + height + (double) sideLength * Math.sin(angle)));
        }
        GL11.glVertex2d((double) (x + (double) sideLength), (double) (y + height));
        GL11.glEnd();
        GL11.glDisable((int) 2848);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GL11.glEnable((int) 2884);
        GL11.glEnable((int) 3553);
        GL11.glDisable((int) 3042);
        color(Color.white);
        rect(x + halfRadius, y + halfRadius, width - halfRadius, height - halfRadius, color);
        rect(x, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + width, y + halfRadius, edgeRadius / 2.0, height - halfRadius, color);
        rect(x + halfRadius, y, width - halfRadius, halfRadius, color);
        rect(x + halfRadius, y + height, width - halfRadius, halfRadius, color);
    }
}
