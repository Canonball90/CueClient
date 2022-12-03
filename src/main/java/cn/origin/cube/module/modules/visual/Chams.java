package cn.origin.cube.module.modules.visual;

import cn.origin.cube.event.events.render.EventModelPlayerRender;
import cn.origin.cube.event.events.render.RenderEntityModelEvent;
import cn.origin.cube.event.events.world.Render3DEvent;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.settings.BooleanSetting;
import cn.origin.cube.settings.FloatSetting;
import cn.origin.cube.settings.IntegerSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "Chams", descriptions = "Always light", category = Category.VISUAL)
public class Chams extends Module {

    private final BooleanSetting lines = registerSetting("Lines", true);
    private final FloatSetting width = registerSetting("OutlineWidth", 40F, 0F, 100F);
    private final IntegerSetting alpha = registerSetting("Alpha", 200, 0, 255);

    @SubscribeEvent
    public void onPlayerModel(final EventModelPlayerRender event) {
        if (fullNullCheck()) return;
        final Color c = new Color(ClickGui.getCurrentColor().getRGB());
        if (event.type == 0) {
            GL11.glPushMatrix();
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);
            if (!this.lines.getValue()) {
                GL11.glPolygonMode(1028, 6914);
            } else {
                GL11.glPolygonMode(1028, 6913);
            }
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.alpha.getValue() / 255.0f / 2.0f);
            if (this.lines.getValue()) {
                GL11.glLineWidth(this.width.getValue() / 10.0f);
            }
        } else if (event.type == 1) {
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 1.0E7f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    public void onCrystalModel(final RenderEntityModelEvent event) {
        if (fullNullCheck()) return;
        final Color c = new Color(ClickGui.getCurrentColor().getRGB());
        if (event.type == 0) {
            GL11.glPushMatrix();
            GL11.glEnable(32823);
            GL11.glPolygonOffset(1.0f, -1.0E7f);
            GL11.glPushAttrib(1048575);
            GL11.glPolygonMode(1028, 6914);
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, this.alpha.getValue() / 255.0f / 2.0f);
            if (this.lines.getValue()) {
                GL11.glLineWidth(this.width.getValue() / 10.0f);
            }
        } else if (event.type == 1) {
            GL11.glPopAttrib();
            GL11.glPolygonOffset(1.0f, 1.0E7f);
            GL11.glDisable(32823);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void onRender3D(Render3DEvent event){

    }
}
