package dev.canon.cue.module.modules.function;

import dev.canon.cue.event.events.EventStage;
import dev.canon.cue.event.events.client.PacketEvent;
import dev.canon.cue.event.events.player.UpdateWalkingPlayerEvent;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.settings.ModeSetting;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.concurrent.ThreadLocalRandom;

@ModuleInfo(name = "NoRotate", descriptions = "Dangerous to use might desync you.", category = Category.FUNCTION)
public class NoRotate extends Module {
    private boolean cancelPackets = true;
    private boolean timerReset = false;

    @Override
    public void onUpdate() {
        if (this.timerReset && !this.cancelPackets) {
            this.cancelPackets = true;
            this.timerReset = false;
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (fullNullCheck()) return;
        if (event.getStage() == 0 && this.cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = event.getPacket();
            packet.yaw = NoRotate.mc.player.rotationYaw;
            packet.pitch = NoRotate.mc.player.rotationPitch;
        }
    }

}

