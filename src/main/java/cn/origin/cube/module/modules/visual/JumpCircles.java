package cn.origin.cube.module.modules.visual;

import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.ModeSetting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Constant
@ModuleInfo(name = "JumpCircle", descriptions = "", category = Category.VISUAL)
public class JumpCircles extends Module {
    static List<Circle> circles = new ArrayList();
    static final byte MAX_JC_TIME = 20;

    public enum modes{Default, Disc}
    ModeSetting mode = registerSetting("Mode", modes.Default);

    @Override
    public void onUpdate(){
        if(fullNullCheck()) return;
        if (mc.player.motionY==0.33319999363422365){
            handleEntityJump(mc.player);
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if(fullNullCheck()) return;
        EntityPlayerSP client = mc.player;
        double ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * mc.getRenderPartialTicks());
        double iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * mc.getRenderPartialTicks());
        double iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * mc.getRenderPartialTicks());
        if (mode.getValue().equals("Default")){
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Collections.reverse(circles);
            try {
                for (Circle c : JumpCircles.circles) {
                    float k = (float) c.existed / MAX_JC_TIME;
                    double x = c.position().x;
                    double y = c.position().y - k * 0.5;
                    double z = c.position().z;
                    float end = k + 1f - k;
                    GL11.glBegin(GL11.GL_QUAD_STRIP);
                    for (int i = 0; i <= 360; i = i + 5) {
                        GL11.glColor4f(255,255,255, 0.2f * (1 - ((float) c.existed / MAX_JC_TIME)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * k, y, z + Math.sin(Math.toRadians(i * 4)) * k);
                        GL11.glColor4f(1, 1, 1, 0.01f * (1 - ((float) c.existed / MAX_JC_TIME)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin(k * 8) * 0.5, z + Math.sin(Math.toRadians(i) * end));
                    }
                    GL11.glEnd();
                }
            } catch (Exception ignored) {}
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        } else if (mode.getValue().equals("Disc")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Collections.reverse(circles);
            try {
                for (Circle c : JumpCircles.circles) {
                    double x = c.position().x;
                    double y = c.position().y;
                    double z = c.position().z;
                    float k = (float) c.existed / MAX_JC_TIME;
                    float start = k * 2.5f;
                    float end = start + 1f - k;
                    GL11.glBegin(GL11.GL_QUAD_STRIP);
                    for (int i = 0; i <= 360; i = i + 5) {
                        GL11.glColor4f((float) c.color().x, (float) c.color().y, (float) c.color().z, 0.7f * (1 - ((float) c.existed / MAX_JC_TIME)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start, y, z + Math.sin(Math.toRadians(i)) * start);
                        GL11.glColor4f(1, 1, 1, 0.01f * (1 - ((float) c.existed / MAX_JC_TIME)));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y, z + Math.sin(Math.toRadians(i)) * end);
                    }
                    GL11.glEnd();
                }
            } catch (Exception ignored) {}
            Collections.reverse(circles);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
    }


    public static void onLocalPlayerUpdate() {
        circles.removeIf(Circle::update);
    }

    public static void handleEntityJump(EntityPlayerSP entity) {
        Vec3d color = new Vec3d(0, 165, 0);
        circles.add(new Circle(entity.getPositionVector(), color));
    }



    static class Circle {
        private final Vec3d vec;
        private final Vec3d color;
        byte existed;

        Circle(Vec3d vec, Vec3d color) {
            this.vec = vec;
            this.color = color;
        }

        Vec3d position() {
            return this.vec;
        }

        Vec3d color() {
            return this.color;
        }

        boolean update() {
            return ++existed > MAX_JC_TIME;
        }
    }
}
