package cn.origin.cube.module.modules.world;

import cn.origin.cube.Cube;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.DoubleSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.render.Render2DUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

import java.awt.*;

@Constant
@ModuleInfo(name = "Jarvis", descriptions = ",",category = Category.WORLD)
public class JarvisModule extends Module {

    FloatSetting height = registerSetting("Height", 5f, 1f, 15f);
    FloatSetting dist = registerSetting("Distance", 100f, 100f, 500f);

    @Override
    public void onEnable(){
        if(fullNullCheck()) return;
        mc.player.eyeHeight=height.getValue();
        mc.getRenderManager().entityRenderMap.put(EntityPig.class, new RenderPig(mc.getRenderManager()));
    }

    @Override
    public void onRender2D() {
        if(fullNullCheck()) return;
        Render2DUtil.drawRect1(0,0, 1000, 1000, new Color(104, 104, 255, 137).getRGB());
        super.onRender2D();
    }

    @Override
    public void onUpdate() {
        if(fullNullCheck()) return;
        EntityPlayer player = null;
        float tickDis = dist.getValue();
        for (EntityPlayer p : mc.world.playerEntities) {
            if (p instanceof EntityPlayerSP) continue;
            float dis = p.getDistance(mc.player);
            if (dis < tickDis) {
                tickDis = dis;
                player = p;
            }
        }
        if (player != null) {
            Vec3d pos = EntityUtil.interpolateEntity(player, mc.getRenderPartialTicks());
            float[] angels = MathUtil.calcAngle(EntityUtil.interpolateEntity(mc.player, mc.getRenderPartialTicks()), new Vec3d(pos.x, pos.y - height.getValue() + 0.5, pos.z));
            //angels[1] -=  calcPitch(tickDis);
            mc.player.rotationYaw = angels[0];
            mc.player.rotationPitch = angels[1];
        }
    }

    @Override
    public void onDisable() {
        mc.player.eyeHeight = mc.player.getDefaultEyeHeight();
        mc.getRenderManager().entityRenderMap.put(EntityPig.class, new RenderPig(mc.getRenderManager()));
    }
}