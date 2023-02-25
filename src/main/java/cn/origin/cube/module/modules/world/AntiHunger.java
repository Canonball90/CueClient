package cn.origin.cube.module.modules.world;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.inject.client.ICPacketPlayer;
import cn.origin.cube.inject.client.IEntityPlayerSP;
import net.minecraft.network.play.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;

@Constant
@ModuleInfo(name = "AntiHunger", descriptions = "", category = Category.WORLD)
public class AntiHunger extends Module {

    BooleanSetting groundSpoof = registerSetting("GroundSpoof", true);
    BooleanSetting stopSprint = registerSetting("StopSprint", true);
    boolean previousSprint;

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send send) {
        if (send.getPacket() instanceof CPacketPlayer) {
            if ((boolean)this.groundSpoof.getValue() && !AntiHunger.mc.player.isRiding() && !AntiHunger.mc.player.isElytraFlying()) {
                ((ICPacketPlayer)send.getPacket()).setOnGround(true);
            }
        }
        else if (send.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction cPacketEntityAction = (CPacketEntityAction)send.getPacket();
            if ((cPacketEntityAction.getAction().equals((Object)CPacketEntityAction.Action.START_SPRINTING) || cPacketEntityAction.getAction().equals((Object)CPacketEntityAction.Action.STOP_SPRINTING)) && (boolean)this.stopSprint.getValue()) {
                send.setCanceled(true);
            }
        }
    }

    @Override
    public void onEnable() {
        if (AntiHunger.mc.player.isSprinting() || ((IEntityPlayerSP)AntiHunger.mc.player).getServerSprintState()) {
            this.previousSprint = true;
            AntiHunger.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AntiHunger.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (this.previousSprint) {
            this.previousSprint = false;
            AntiHunger.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)AntiHunger.mc.player, CPacketEntityAction.Action.START_SPRINTING));
        }
    }
}
