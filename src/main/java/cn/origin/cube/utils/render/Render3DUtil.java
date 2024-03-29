package cn.origin.cube.utils.render;

import cn.origin.cube.utils.player.EntityUtil;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public class Render3DUtil extends Tessellator {
    private static final Render3DUtil INSTANCE = new Render3DUtil();
    public static Minecraft mc = Minecraft.getMinecraft();
    public static ICamera camera = new Frustum();
    public static Tessellator tessellator = Tessellator.getInstance();
    public static BufferBuilder bufferBuilder = tessellator.getBuffer();

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

    public static void prepare() {
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth((float)1.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
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

    public static void drawBox(final BlockPos pos, final Color color, final double height, final boolean gradient, final boolean invert, final int alpha) {
        if (gradient) {
            final Color endColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
            drawOpenGradientBox(pos, invert ? endColor : color, invert ? color : endColor, height);
            return;
        }
        final AxisAlignedBB bb = new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ, pos.getX() + 1 - mc.getRenderManager().viewerPosX, pos.getY() + 1 - mc.getRenderManager().viewerPosY + height, pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
        RenderUtil.camera.setPosition(Objects.requireNonNull(mc.getRenderViewEntity()).posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        if (RenderUtil.camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + mc.getRenderManager().viewerPosX, bb.minY + mc.getRenderManager().viewerPosY, bb.minZ + mc.getRenderManager().viewerPosZ, bb.maxX + mc.getRenderManager().viewerPosX, bb.maxY + mc.getRenderManager().viewerPosY, bb.maxZ + mc.getRenderManager().viewerPosZ))) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(bb, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glDisable(2848);
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawOpenGradientBox(final BlockPos pos, final Color startColor, final Color endColor, final double height) {
        for (final EnumFacing face : EnumFacing.values()) {
            if (face != EnumFacing.UP) {
                drawGradientPlane(pos, face, startColor, endColor, height);
            }
        }
    }

    public static void drawGradientPlane(final BlockPos pos, final EnumFacing face, final Color startColor, final Color endColor, final double height) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder builder = tessellator.getBuffer();
        final IBlockState iblockstate = mc.world.getBlockState(pos);
        final Vec3d interp = EntityUtil.interpolateEntity((Entity) mc.player, mc.getRenderPartialTicks());
        final AxisAlignedBB bb = iblockstate.getSelectedBoundingBox((World) mc.world, pos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z).expand(0.0, height, 0.0);
        final float red = startColor.getRed() / 255.0f;
        final float green = startColor.getGreen() / 255.0f;
        final float blue = startColor.getBlue() / 255.0f;
        final float alpha = startColor.getAlpha() / 255.0f;
        final float red2 = endColor.getRed() / 255.0f;
        final float green2 = endColor.getGreen() / 255.0f;
        final float blue2 = endColor.getBlue() / 255.0f;
        final float alpha2 = endColor.getAlpha() / 255.0f;
        double x1 = 0.0;
        double y1 = 0.0;
        double z1 = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;
        double z2 = 0.0;
        if (face == EnumFacing.DOWN) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.minY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        }
        else if (face == EnumFacing.UP) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.maxY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        }
        else if (face == EnumFacing.EAST) {
            x1 = bb.maxX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        }
        else if (face == EnumFacing.WEST) {
            x1 = bb.minX;
            x2 = bb.minX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.maxZ;
        }
        else if (face == EnumFacing.SOUTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.maxZ;
            z2 = bb.maxZ;
        }
        else if (face == EnumFacing.NORTH) {
            x1 = bb.minX;
            x2 = bb.maxX;
            y1 = bb.minY;
            y2 = bb.maxY;
            z1 = bb.minZ;
            z2 = bb.minZ;
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.depthMask(false);
        builder.begin(5, DefaultVertexFormats.POSITION_COLOR);
        if (face == EnumFacing.EAST || face == EnumFacing.WEST || face == EnumFacing.NORTH || face == EnumFacing.SOUTH) {
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
        }
        else if (face == EnumFacing.UP) {
            builder.pos(x1, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y1, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y1, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x1, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z1).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
            builder.pos(x2, y2, z2).color(red2, green2, blue2, alpha2).endVertex();
        }
        else if (face == EnumFacing.DOWN) {
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y1, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x1, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z1).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
            builder.pos(x2, y2, z2).color(red, green, blue, alpha).endVertex();
        }
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public static void drawCircle(RenderBuilder renderBuilder, Vec3d vec3d, double radius, double height, Color color) {
        renderCircle(bufferBuilder, vec3d, radius, height, color);
        renderBuilder.build();
    }

    public static void renderCircle(BufferBuilder bufferBuilder, Vec3d vec3d, double radius, double height, Color color) {
        GlStateManager.disableCull();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL_SMOOTH);
        bufferBuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        for (int i = 0; i < 361; i++) {
            bufferBuilder.pos((vec3d.x) + Math.sin(Math.toRadians(i)) * radius - Minecraft.getMinecraft().getRenderManager().viewerPosX, vec3d.y + height - Minecraft.getMinecraft().getRenderManager().viewerPosY, ((vec3d.z) + Math.cos(Math.toRadians(i)) * radius) - Minecraft.getMinecraft().getRenderManager().viewerPosZ).color((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, 1).endVertex();
        }

        tessellator.draw();

        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(GL_FLAT);
    }

    public static void drawBox(RenderBuilder renderBuilder) {
        if (mc.getRenderViewEntity() != null) {

            AxisAlignedBB axisAlignedBB = renderBuilder.getAxisAlignedBB()
                    .offset(-mc.getRenderManager().viewerPosX, -mc.getRenderManager().viewerPosY, -mc.getRenderManager().viewerPosZ);

            switch (renderBuilder.getBox()) {
                case FILL:
                    drawSelectionBox(axisAlignedBB, renderBuilder.getHeight(), renderBuilder.getLength(), renderBuilder.getWidth(), renderBuilder.getColor());
                    break;
                case OUTLINE:
                    drawSelectionBoundingBox(axisAlignedBB, renderBuilder.getHeight(), renderBuilder.getLength(), renderBuilder.getWidth(), new Color(renderBuilder.getColor().getRed(), renderBuilder.getColor().getGreen(), renderBuilder.getColor().getBlue(), 144));
                    break;
                case BOTH:
                    drawSelectionBox(axisAlignedBB, renderBuilder.getHeight(), renderBuilder.getLength(), renderBuilder.getWidth(), renderBuilder.getColor());
                    drawSelectionBoundingBox(axisAlignedBB, renderBuilder.getHeight(), renderBuilder.getLength(), renderBuilder.getWidth(), new Color(renderBuilder.getColor().getRed(), renderBuilder.getColor().getGreen(), renderBuilder.getColor().getBlue(), 144));
                    break;
                case GLOW:
                    drawSelectionGlowFilledBox(axisAlignedBB, renderBuilder.getHeight(), renderBuilder.getLength(), renderBuilder.getWidth(), renderBuilder.getColor(), new Color(renderBuilder.getColor().getRed(), renderBuilder.getColor().getGreen(), renderBuilder.getColor().getBlue(), 0));
                    break;
                case REVERSE:
                    drawSelectionGlowFilledBox(axisAlignedBB, renderBuilder.getHeight(), renderBuilder.getLength(), renderBuilder.getWidth(), new Color(renderBuilder.getColor().getRed(), renderBuilder.getColor().getGreen(), renderBuilder.getColor().getBlue(), 0), renderBuilder.getColor());
                    break;
                case CLAW:
                    drawClawBox(axisAlignedBB, renderBuilder.getHeight(), renderBuilder.getLength(), renderBuilder.getWidth(), new Color(renderBuilder.getColor().getRed(), renderBuilder.getColor().getGreen(), renderBuilder.getColor().getBlue(), 255));
                    break;
            }
            renderBuilder.build();
        }
    }

    public static void drawSelectionBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color color) {
        bufferBuilder.begin(GL_TRIANGLE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        addChainedFilledBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, color);

        tessellator.draw();
    }

    public static void addChainedFilledBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {

        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color color) {

        bufferBuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        addChainedBoundingBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, color);
        tessellator.draw();
    }

    public static void addChainedBoundingBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {

        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
    }

    public static void drawSelectionGlowFilledBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color startColor, Color endColor) {

        bufferBuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        addChainedGlowBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, startColor, endColor);
        tessellator.draw();
    }

    public static void addChainedGlowBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color startColor, Color endColor) {

        bufferBuilder.pos(minX, minY, minZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(startColor.getRed() / 255F, startColor.getGreen() / 255F, startColor.getBlue() / 255F, startColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(endColor.getRed() / 255F, endColor.getGreen() / 255F, endColor.getBlue() / 255F, endColor.getAlpha() / 255F).endVertex();
    }

    public static void drawClawBox(AxisAlignedBB axisAlignedBB, double height, double length, double width, Color color) {

        bufferBuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        addChainedClawBoxVertices(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ, axisAlignedBB.maxX + length, axisAlignedBB.maxY + height, axisAlignedBB.maxZ + width, color);
        tessellator.draw();
    }

    public static void addChainedClawBoxVertices(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Color color) {

        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, minY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, minY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, minY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX - 0.8, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX - 0.8, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX + 0.8, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX + 0.8, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, minY + 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, minY + 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, minY + 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, minY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, minY + 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, maxY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ - 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ + 0.8).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX - 0.8, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX - 0.8, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX + 0.8, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX + 0.8, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, maxY - 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(minX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(minX, maxY - 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, maxY - 0.2, minZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos(maxX, maxY, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), 0).endVertex();
        bufferBuilder.pos(maxX, maxY - 0.2, maxZ).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void releaseq() {
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }


    public static void drawBlock(BlockPos position, Color color) {
        drawBlock(getRenderBB((Object)position), color);
    }

    public static void drawBlock(AxisAlignedBB bb, Color color) {
        camera.setPosition(Objects.requireNonNull(Render3DUtil.mc.getRenderViewEntity()).posX, Render3DUtil.mc.getRenderViewEntity().posY, Render3DUtil.mc.getRenderViewEntity().posZ);
        if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(bb.minX + Render3DUtil.mc.getRenderManager().viewerPosX, bb.minY + Render3DUtil.mc.getRenderManager().viewerPosY, bb.minZ + Render3DUtil.mc.getRenderManager().viewerPosZ, bb.maxX + Render3DUtil.mc.getRenderManager().viewerPosX, bb.maxY + Render3DUtil.mc.getRenderManager().viewerPosY, bb.maxZ + Render3DUtil.mc.getRenderManager().viewerPosZ))) {
            prepareGL();
            RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
            Render3DUtil.releaseq();
        }
    }

    public static void drawRect(float x, float y, float width, float height, Color color) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferBuilder.pos((double)x, (double)height, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos((double)width, (double)height, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos((double)width, (double)y, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        bufferBuilder.pos((double)x, (double)y, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void glColor4f(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

}
