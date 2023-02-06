package cn.origin.cube.module.modules.visual;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.core.events.event.event.ParallelListener;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
@Constant(constant = false)
@ParallelListener
@ModuleInfo(name = "ChorusViewer", descriptions = "", category = Category.VISUAL)
public class ChorusViewer extends Module {

    IntegerSetting timeToRemove = registerSetting("RemoveTime", 2000, 1000, 5000);
    private final Timer timer = new Timer();
    private BlockPos chorusPos;

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
                chorusPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                timer.reset();
            }
        }
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (chorusPos != null) {
            if (timer.passed(timeToRemove.getValue())) {
                chorusPos = null;
                return;
            }
            Render3DUtil.drawBlockBox(chorusPos, new Color(Colors.getGlobalColor().getRed(), Colors.getGlobalColor().getGreen(), Colors.getGlobalColor().getBlue(), 150), true, 2F);
        }
    }
}
