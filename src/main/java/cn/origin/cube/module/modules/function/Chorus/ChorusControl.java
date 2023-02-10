package cn.origin.cube.module.modules.function.Chorus;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.utils.render.Render3DUtil;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@Constant
@ModuleInfo(name = "ChorusControl", descriptions = "", category = Category.FUNCTION)
public class ChorusControl extends Module {

    BooleanSetting cpacketplayer = registerSetting("cpacketplayer", false);
    BooleanSetting spacketplayerposlook = registerSetting("spacketplayerposlook", false);
    BooleanSetting render = registerSetting("render", false);

    Queue<CPacketPlayer> packets = new LinkedList<>();
    Queue<CPacketConfirmTeleport> teleportPackets = new LinkedList<>();

    SPacketPlayerPosLook sPacketPlayerPosLook;

    @SubscribeEvent
    public void send(PacketEvent.Send event){
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            sPacketPlayerPosLook = (SPacketPlayerPosLook) event.getPacket();
            if (spacketplayerposlook.getValue()) event.setCanceled(true);
        }

        if (event.getPacket() instanceof CPacketPlayer) {
            packets.add(((CPacketPlayer) event.getPacket()));

            if (cpacketplayer.getValue())
                event.setCanceled(true);
        }

        if (event.getPacket() instanceof CPacketConfirmTeleport) {
            teleportPackets.add(((CPacketConfirmTeleport) event.getPacket()));
            event.setCanceled(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        while (!this.packets.isEmpty()) {
            mc.getConnection().sendPacket(Objects.requireNonNull(this.packets.poll()));
        }
        while (!this.teleportPackets.isEmpty()) {
            mc.getConnection().sendPacket(Objects.requireNonNull(this.teleportPackets.poll()));
        }
        sPacketPlayerPosLook = null;
    }

    @Override
    public void onRender3D(Render3DEvent event){
        if (sPacketPlayerPosLook == null) return;
        if (!render.getValue()) return;

        BlockPos pos = new BlockPos(sPacketPlayerPosLook.getX(), sPacketPlayerPosLook.getY(), sPacketPlayerPosLook.getZ());
        Render3DUtil.drawBlockBox(pos, Color.RED,true,1);
    }
}
