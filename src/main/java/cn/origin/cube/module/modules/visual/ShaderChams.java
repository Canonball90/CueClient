package cn.origin.cube.module.modules.visual;

import cn.origin.cube.event.events.world.Render3DEvent;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;
import cn.origin.cube.settings.BooleanSetting;
import cn.origin.cube.settings.ModeSetting;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.render.shader.FramebufferShader;
import cn.origin.cube.utils.render.shader.shaders.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@ModuleInfo(name = "ShaderChams", descriptions = "Europa except I made it not actually dogshit.", category = Category.VISUAL)
public class ShaderChams extends Module {
    public static ShaderChams INSTANCE;
    public ModeSetting<ShaderModes> shaderMode = registerSetting("ShaderMode", ShaderModes.AQUA);
    public enum ShaderModes {AQUA, RED, SMOKE, FLOW,RB}
    public BooleanSetting targetParent = registerSetting("Targets", true);
    public BooleanSetting players = registerSetting("PLayers", true);
    public BooleanSetting crystals = registerSetting("Crystals", true);
    public BooleanSetting mobs = registerSetting("Mobs", false);
    public BooleanSetting animals = registerSetting("Animals", false);

    public ShaderChams() {
        INSTANCE = this;
    }

    public static ShaderChams getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ShaderChams();
        }
        return INSTANCE;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        FramebufferShader framebufferShader = null;
        if (shaderMode.getValue().equals(ShaderModes.RB))
            framebufferShader = RainbowOutlineShader.RAINBOW_OUTLINE_SHADER;

        if (framebufferShader == null)
            return;
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        framebufferShader.startDraw(event.getPartialTicks());
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity == mc.player || entity == mc.getRenderViewEntity())
                continue;
            if (!(entity instanceof EntityPlayer))
                continue;
            Vec3d vector = MathUtil.getInterpolatedRenderPos(entity, event.getPartialTicks());
            Objects.requireNonNull(mc.getRenderManager().getEntityRenderObject(entity)).doRender(entity, vector.x, vector.y, vector.z, entity.rotationYaw, event.getPartialTicks());
        }
        framebufferShader.stopDraw();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
    }
}
