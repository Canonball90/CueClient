package cn.origin.cube.utils.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class Render3DUtil extends Tessellator {
    private static final Render3DUtil INSTANCE = new Render3DUtil();
    public static Minecraft mc = Minecraft.getMinecraft();
    public static ICamera camera = new Frustum();

    public Render3DUtil() {
        super(0x200000);
    }

    public static void prepare(int mode) {
        prepareGL();
        begin(mode);
    }

    public static void release() {
        render();
        releaseGL();
    }

    public static void render() {
        INSTANCE.draw();
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void begin(int mode) {
        INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
    }

    public static void drawBlockBox(BlockPos pos, Color color, boolean outline, float lineWidth) {
        drawBBBox(new AxisAlignedBB(pos), color, color.getAlpha(), lineWidth, outline);
    }

    public static void drawBoxOutline(BlockPos pos,Color color,float lineWidth){
        drawBoundingBox(new AxisAlignedBB(pos), lineWidth, color.getRed(), color.getGreen(), color.getBlue(), 255);
    }

    public static void drawBBBox(AxisAlignedBB BB, Color colour, int alpha, float lineWidth, boolean outline) {
        AxisAlignedBB bb = new AxisAlignedBB(BB.minX - mc.getRenderManager().viewerPosX, BB.minY - mc.getRenderManager().viewerPosY, BB.minZ - mc.getRenderManager().viewerPosZ, BB.maxX - mc.getRenderManager().viewerPosX, BB.maxY - mc.getRenderManager().viewerPosY, BB.maxZ - mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ))) {
            prepare(GL_QUADS);
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glEnable(GL_LINE_SMOOTH);
            GL11.glHint(3154, 4354);
            GL11.glShadeModel(GL_SMOOTH);
            if (outline) {
                drawBoundingBox(bb, lineWidth, colour.getRed(), colour.getGreen(), colour.getBlue(), 255);
            }
            RenderGlobal.renderFilledBox(bb, (float) colour.getRed() / 255.0f, (float) colour.getGreen() / 255.0f, (float) colour.getBlue() / 255.0f, alpha / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            release();
        }
    }

    public static void drawBoundingBox(AxisAlignedBB bb, float width, int red, int green, int blue, int alpha) {
        GL11.glLineWidth(width);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.color(red / 255f, green / 255f, blue / 255f, alpha / 255f);
        drawBoundingBox(bb);
        GlStateManager.color(1, 1, 1, 1);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }

    public static void drawBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexBuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexBuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexBuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexBuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void prepareGL() {
        GL11.glBlendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    public static void drawBoxESPFlat(BlockPos pos, boolean box, boolean outline, Color color, Color outlineColor, float lineWidth) {
        if (box)
            drawBoxFlat(pos, color);
        if (outline)
            drawBlockOutlineFlat(pos, outlineColor, lineWidth);
    }

    public static void drawBlockOutlineFlat(final BlockPos pos, final Color color, final float lineWidth) {
        final IBlockState iblockstate = mc.world.getBlockState(pos);
        if (mc.world.getWorldBorder().contains(pos)) {
            final Vec3d interp = interpolateEntity(mc.player, mc.getRenderPartialTicks());
            drawBlockOutlineFlat(iblockstate.getSelectedBoundingBox(mc.world, pos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), color, lineWidth);
        }
    }

    public static void drawBoxFlat(final BlockPos pos, final Color color) {
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ, pos.getX() + 1 - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            glEnable(2848);
            glHint(3154, 4354);
            RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBlockOutlineFlat(final AxisAlignedBB bb, final Color color, final float lineWidth) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alpha = color.getAlpha() / 255.0f;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        glEnable(2848);
        glHint(3154, 4354);
        GL11.glLineWidth(lineWidth);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static Vec3d interpolateEntity(Entity entity, float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)time);
    }

    public static AxisAlignedBB getRenderBB(Object position) {
        if (position instanceof BlockPos) {
            return new AxisAlignedBB((double)((BlockPos)position).getX() - Render3DUtil.mc.getRenderManager().viewerPosX, (double)((BlockPos)position).getY() - Render3DUtil.mc.getRenderManager().viewerPosY, (double)((BlockPos)position).getZ() - Render3DUtil.mc.getRenderManager().viewerPosZ, (double)(((BlockPos)position).getX() + 1) - Render3DUtil.mc.getRenderManager().viewerPosX, (double)(((BlockPos)position).getY() + 1) - Render3DUtil.mc.getRenderManager().viewerPosY, (double)(((BlockPos)position).getZ() + 1) - Render3DUtil.mc.getRenderManager().viewerPosZ);
        }
        if (position instanceof AxisAlignedBB) {
            return new AxisAlignedBB(((AxisAlignedBB)position).minX - Render3DUtil.mc.getRenderManager().viewerPosX, ((AxisAlignedBB)position).minY - Render3DUtil.mc.getRenderManager().viewerPosY, ((AxisAlignedBB)position).minZ - Render3DUtil.mc.getRenderManager().viewerPosZ, ((AxisAlignedBB)position).maxX - Render3DUtil.mc.getRenderManager().viewerPosX, ((AxisAlignedBB)position).maxY - Render3DUtil.mc.getRenderManager().viewerPosY, ((AxisAlignedBB)position).maxZ - Render3DUtil.mc.getRenderManager().viewerPosZ);
        }
        return null;
    }
}
