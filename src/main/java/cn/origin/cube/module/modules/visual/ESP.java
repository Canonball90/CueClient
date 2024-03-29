package cn.origin.cube.module.modules.visual;

import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import java.awt.Color;

import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.core.settings.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
@Constant(constant = false)
@ModuleInfo(name = "ESP",
        descriptions = "ESP",
        category = Category.VISUAL)
public class ESP extends Module {
    ModeSetting mode = registerSetting("Mode", Mode.TwoDee);
    BooleanSetting hunger = registerSetting("Hunger", true);
    BooleanSetting health = registerSetting("Health", true);
    BooleanSetting healthValue = registerSetting("healthValue", true);
    BooleanSetting box = registerSetting("Box", true);
    BooleanSetting tag = registerSetting("Tag", true);
    BooleanSetting showItem = registerSetting("Show current item", true);
    BooleanSetting local = registerSetting("Local", false);
    FloatSetting alpha = registerSetting("3DAlpha", 0.7f, 0.0f, 1.0f);


    @Override
    public void onDisable() {
        super.onDisable();
        for (Entity entity : ESP.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityPlayer) || !entity.isGlowing()) continue;
            entity.setGlowing(false);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {

        if (mode.getValue().equals(Mode.TwoDee)) {
            for (Entity e : ESP.mc.world.loadedEntityList) {
                if (!(e instanceof EntityPlayer) || e == ESP.mc.player && (!local.getValue() || ESP.mc.gameSettings.thirdPersonView != 1)) continue;
                double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * (double)event.getPartialTicks() - ESP.mc.getRenderManager().viewerPosX;
                double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * (double)event.getPartialTicks() - ESP.mc.getRenderManager().viewerPosY;
                double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * (double)event.getPartialTicks() - ESP.mc.getRenderManager().viewerPosZ;
                GL11.glPushMatrix();
                GL11.glLineWidth((float)1.3f);
                GL11.glTranslated((double)x, (double)y, (double)z);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)2929);
                GL11.glRotated((double)(-ESP.mc.getRenderManager().playerViewY), (double)0.0, (double)1.0, (double)0.0);
                if (box.getValue()) {
                    GL11.glColor4d(255,255,255,255);
                    GL11.glBegin((int)3);
                    GL11.glVertex3d((double)0.55, (double)-0.2, (double)0.0);
                    GL11.glVertex3d((double)0.55, (double)((double)e.height + 0.2), (double)0.0);
                    GL11.glVertex3d((double)((double)e.width - 1.15), (double)((double)e.height + 0.2), (double)0.0);
                    GL11.glVertex3d((double)((double)e.width - 1.15), (double)-0.2, (double)0.0);
                    GL11.glVertex3d((double)0.55, (double)-0.2, (double)0.0);
                    GL11.glEnd();
                }
                if (health.getValue()) {
                    Color health = Color.GREEN.darker();
                    if (((EntityPlayer)e).getHealth() >= 16.0f) {
                        health = Color.GREEN.darker();
                    } else if (((EntityPlayer)e).getHealth() >= 8.0f && ((EntityPlayer)e).getHealth() <= 16.0f) {
                        health = Color.YELLOW;
                    } else if (((EntityPlayer)e).getHealth() > 0.0f && ((EntityPlayer)e).getHealth() <= 8.0f) {
                        health = Color.RED;
                    }
                    GL11.glLineWidth((float)4.0f);
                    GL11.glBegin((int)3);
                    GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
                    GL11.glVertex3d((double)0.6, (double)-0.2, (double)0.0);
                    GL11.glVertex3d((double)0.6, (double)((double)e.height + 0.2), (double)0.0);
                    GL11.glEnd();
                    GL11.glBegin((int)3);
                    GL11.glColor4d((double)((float)health.getRed() / 255.0f), (double)((float)health.getGreen() / 255.0f), (double)((float)health.getBlue() / 255.0f), (double)((float)health.getAlpha() / 255.0f));
                    GL11.glVertex3d((double)0.6, (double)-0.2, (double)0.0);
                    GL11.glVertex3d((double)0.6, (double)((double)(((EntityLivingBase)e).getHealth() / ((EntityLivingBase)e).getMaxHealth()) * ((double)e.height + 0.2)), (double)0.0);
                    GL11.glVertex3d((double)0.6, (double)-0.2, (double)0.0);
                    GL11.glLineWidth((float)1.0f);
                    GL11.glEnd();
                }
                if (hunger.getValue()) {
                    GL11.glBegin((int)3);
                    GL11.glVertex3d((double)((double)e.width - 1.2), (double)((double)e.height + 0.2), (double)0.0);
                    GL11.glVertex3d((double)((double)e.width - 1.2), (double)-0.2, (double)0.0);
                    GL11.glColor4d((double)Color.ORANGE.getRed(), (double)Color.ORANGE.getGreen(), (double)Color.ORANGE.getBlue(), (double)255.0);
                    GL11.glVertex3d((double)((double)e.width - 1.2), (double)((double)e.height + 0.2), (double)0.0);
                    GL11.glVertex3d((double)((double)e.width - 1.2), (double)-0.2, (double)0.0);
                    GL11.glColor4d((double)255.0, (double)255.0, (double)255.0, (double)255.0);
                    GL11.glEnd();
                }
                float size = 0.013f;
                GL11.glScaled((double)-0.013f, (double)-0.013f, (double)-0.013f);
                if (tag.getValue()) {
                    GL11.glEnable((int)3553);
                    ESP.mc.fontRenderer.drawStringWithShadow(e.getName(), (float) (1 - ESP.mc.fontRenderer.getStringWidth(e.getName()) / 2), -170.0f, -1);
                    GL11.glDisable((int)3553);
                }
                if (healthValue.getValue()) {
                    if(health.getValue()) {
                        GL11.glEnable((int) 3553);
                        ESP.mc.fontRenderer.drawStringWithShadow(String.valueOf((int) (((EntityPlayer) e).getHealth() / ((EntityPlayer) e).getMaxHealth() * 100.0f)), (float) (-50 - ESP.mc.fontRenderer.getStringWidth(String.valueOf((int) (((EntityPlayer) e).getHealth() / ((EntityPlayer) e).getMaxHealth() * 100.0f)))), (float) ((int) ((double) (((EntityLivingBase) e).getHealth() / ((EntityLivingBase) e).getMaxHealth()) * ((double) e.height + 0.2))), -1);
                        GL11.glDisable((int) 3553);
                    }
                }
                if (showItem.getValue() && !(((EntityPlayer)e).getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) && !(((EntityPlayer)e).getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemAir)) {
                    GL11.glEnable((int)3553);
                    ESP.mc.fontRenderer.drawStringWithShadow(((EntityPlayer) e).getHeldItem(EnumHand.MAIN_HAND).getDisplayName(), (float) (1 - ESP.mc.fontRenderer.getStringWidth(((EntityPlayer) e).getHeldItem(EnumHand.MAIN_HAND).getDisplayName()) / 2), 20.0f, -1);
                    GL11.glDisable((int)3553);
                }
                GL11.glEnable((int)2929);
                GL11.glEnable((int)3553);
                GL11.glPopMatrix();
            }
        } else if (mode.getValue().equals(Mode.Glow)) {
            for (Entity entity : ESP.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity == ESP.mc.player || entity.isGlowing()) continue;
                entity.setGlowing(true);
            }
        } else if (mode.getValue().equals(Mode.ThreeDee)) {
            GlStateManager.pushMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
            GlStateManager.disableDepth();
            for (Entity entity : ESP.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityPlayer) || entity == ESP.mc.player) continue;
                ESP.renderEntityBoundingBox(entity, (float)Color.RED.getRed() / 255.0f, (float)Color.RED.getGreen() / 255.0f, (float)Color.RED.getBlue() / 255.0f, alpha.getValue());
            }
            GlStateManager.enableDepth();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.popMatrix();
        }
    }

    public static void renderEntityFilledBoundingBox(Entity entity, float red, float green, float blue, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        Vec3d entityPos = ESP.interpolateEntity(entity);
        AxisAlignedBB bb = new AxisAlignedBB(entityPos.x - (double)(entity.width / 2.0f), entityPos.y, entityPos.z - (double)(entity.width / 2.0f), entityPos.x + (double)(entity.width / 2.0f), entityPos.y + (double)entity.height, entityPos.z + (double)(entity.width / 2.0f)).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.renderFilledBox((AxisAlignedBB)bb, (float)red, (float)green, (float)blue, (float)alpha);
    }

    public static Vec3d interpolateEntity(Entity entity) {
        double partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks);
    }

    public static void renderEntityBoundingBox(Entity entity, float red, float green, float blue, float alpha) {
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        Vec3d entityPos = ESP.interpolateEntity(entity);
        GlStateManager.glLineWidth((float)5.0f);
        AxisAlignedBB bb = new AxisAlignedBB(entityPos.x - (double)(entity.width / 2.0f), entityPos.y, entityPos.z - (double)(entity.width / 2.0f), entityPos.x + (double)(entity.width / 2.0f), entityPos.y + (double)entity.height, entityPos.z + (double)(entity.width / 2.0f)).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.drawSelectionBoundingBox((AxisAlignedBB)bb, (float)red, (float)green, (float)blue, (float)alpha);
        GlStateManager.glLineWidth((float)1.0f);
    }


    public enum Mode{
        TwoDee,ThreeDee,Glow
    }
}
