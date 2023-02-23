package cn.origin.cube.module.modules.world;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.inject.client.ICPacketPlayer;
import cn.origin.cube.utils.Timer;
import io.netty.buffer.Unpooled;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Constant
@ModuleInfo(name = "Disabler", descriptions = "", category = Category.WORLD)
public class Disabler extends Module {

    private final LinkedBlockingQueue<Packet<INetHandlerPlayServer>> packets = new LinkedBlockingQueue();
    public Timer timer = new Timer();
    public enum Mode{Tubnet,Verus}
    ModeSetting<Mode> mode = registerSetting("Mode", Mode.Tubnet);

    @Override
    public void onEnable() {
        this.timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (fullNullCheck()) {
            return;
        }
        if (this.mode.getValue().equals(Mode.Tubnet)) {
            this.blink();
        }
        super.onDisable();
    }

    @Override
    public void onUpdate() {
        if (this.mode.getValue().equals(Mode.Tubnet)) {
            if (this.timer.passedMs(60L)) {
                this.blink();
                this.timer.reset();
            }
        } else if (this.mode.getValue().equals(Mode.Verus) && this.timer.passedMs(490L)) {
            if (!this.packets.isEmpty()) {
                Disabler.mc.player.connection.sendPacket(this.packets.poll());
            }
            this.timer.reset();
        }
    }

    @SubscribeEvent
    public void onSend(PacketEvent.Send e) {
        if (this.fullNullCheck()) {
            return;
        }
        if (this.mode.getValue().equals(Mode.Tubnet)) {
            Packet<?> packet = e.getPacket();
            if (!(packet instanceof CPacketPlayer) || Disabler.mc.player.ticksExisted % 15 != 0) {
                return;
            }
            try {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                out.writeUTF(Disabler.mc.player.getGameProfile().getName());
                PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
                buf.writeBytes(b.toByteArray());
                Disabler.mc.player.connection.sendPacket((Packet) new CPacketCustomPayload("matrix:geyser", buf));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (packet instanceof CPacketPlayer) {
                e.setCanceled(true);
            }
            if (packet instanceof CPacketPlayer.Position || packet instanceof CPacketPlayer.Rotation || packet instanceof CPacketPlayerTryUseItemOnBlock || packet instanceof CPacketAnimation || packet instanceof CPacketEntityAction || packet instanceof CPacketUseEntity) {
                e.setCanceled(true);
                this.packets.add((Packet<INetHandlerPlayServer>) packet);
            }
        } else if (this.mode.getValue().equals(Mode.Verus)) {
            if (e.getPacket() instanceof CPacketConfirmTransaction) {
                CPacketConfirmTransaction cPacketConfirmTransaction = (CPacketConfirmTransaction) e.getPacket();
                this.packets.add((Packet<INetHandlerPlayServer>) cPacketConfirmTransaction);
                e.setCanceled(true);
            }
            if (e.getPacket() instanceof CPacketPlayer) {
                CPacketPlayer cPacketPlayer = (CPacketPlayer) e.getPacket();
                if (Disabler.mc.player.ticksExisted % 40 == 0) {
                    ((ICPacketPlayer) cPacketPlayer).setY(-0.911);
                    ((ICPacketPlayer) cPacketPlayer).setOnGround(false);
                }
            }
            if (Disabler.mc.player != null && Disabler.mc.player.ticksExisted <= 7) {
                this.timer.reset();
                this.packets.clear();
            }
        }
    }

    private void blink() {
        try {
            while (!this.packets.isEmpty()) {
                Disabler.mc.player.connection.sendPacket(this.packets.take());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
