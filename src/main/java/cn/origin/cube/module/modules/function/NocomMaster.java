package cn.origin.cube.module.modules.function;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.utils.client.ChatUtil;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.DecimalFormat;

@ModuleInfo(name = "NocomMatser", descriptions = "", category = Category.FUNCTION)
public class NocomMaster extends Module {

    public static NocomMaster INSTANCE;

    public void NocomMaster(){
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketBlockChange) {
            if (mc.world.isBlockLoaded(((SPacketBlockChange) event.packet).getBlockPosition(), false)) {
                return;
            }
            SPacketBlockChange packetIn = (SPacketBlockChange) event.packet;
            DecimalFormat df = new DecimalFormat("#.#");
            Vec3d pos1 = new Vec3d(mc.player.getPosition().getX(), packetIn.getBlockPosition().getY(), mc.player.getPosition().getZ());
            ChatUtil.sendMessage("[NoCom]: " + packetIn.getBlockPosition() + " -> " + packetIn.getBlockState().getBlock().getLocalizedName() + " (" + df.format(pos1.distanceTo(new Vec3d(packetIn.getBlockPosition()))) + ") " + (mc.player.dimension == - 1 ? "Nether" : ""));
        }
    }
}
