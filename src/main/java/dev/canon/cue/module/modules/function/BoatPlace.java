package dev.canon.cue.module.modules.function;

import dev.canon.cue.event.events.client.PacketEvent;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleInfo(name = "BoatPlace",
        descriptions = "BoatPlace",
        category = Category.FUNCTION)
public class BoatPlace extends Module {
    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            if (mc.player.getHeldItemMainhand().getItem() == Items.BOAT) {
                event.setCanceled(true);
            }
        }
    }
}
