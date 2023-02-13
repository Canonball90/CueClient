package cn.origin.cube.module.modules.movement;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.event.event.Parallel;
import cn.origin.cube.core.events.player.TravelEvent;
import cn.origin.cube.core.events.world.EntityControlEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.utils.player.EntityUtil;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.passive.AbstractChestHorse;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Parallel
@Constant
@ModuleInfo(name = "EntityControl", descriptions = "", category = Category.MOVEMENT)
public class EntityControl extends Module {

    BooleanSetting mountBypass = registerSetting("MountBypass", false);
    BooleanSetting speedModify = registerSetting("SpeedModify", true);
    FloatSetting speed = registerSetting("Speed", 2.0f, 0.0f, 10.0f);

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (mountBypass.getValue() && event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() != CPacketUseEntity.Action.INTERACT_AT
                && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof AbstractChestHorse || mountBypass.getValue() && event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() != CPacketUseEntity.Action.INTERACT_AT
                && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityPig || mountBypass.getValue() && event.getPacket() instanceof CPacketUseEntity && ((CPacketUseEntity) event.getPacket()).getAction() != CPacketUseEntity.Action.INTERACT_AT
                && ((CPacketUseEntity) event.getPacket()).getEntityFromWorld(mc.world) instanceof EntityDonkey) {
            event.setCanceled(true);
        }

    }

    @SubscribeEvent
    public void entityControlSet(EntityControlEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onTravel(TravelEvent event) {
        if (mc.world != null && mc.player != null && mc.player.ridingEntity != null) {
            if ((mc.player.ridingEntity instanceof EntityPig || mc.player.ridingEntity instanceof AbstractHorse || mc.player.ridingEntity instanceof EntityBoat)
                    && mc.player.ridingEntity.getControllingPassenger() == mc.player) {
                double motionX;
                double motionZ;

                if(speedModify.getValue()){
                    motionX = -Math.sin(EntityUtil.getMovementYaw()) * speed.getValue();
                    motionZ = Math.cos(EntityUtil.getMovementYaw()) * speed.getValue();
                }else{
                    if(mc.player.ridingEntity instanceof EntityPig){
                        motionX = -Math.sin(EntityUtil.getMovementYaw()) * 0.3;
                        motionZ = Math.cos(EntityUtil.getMovementYaw()) * 0.3;
                    }else if(mc.player.ridingEntity instanceof AbstractHorse){
                        motionX = -Math.sin(EntityUtil.getMovementYaw()) * 0.6;
                        motionZ = Math.cos(EntityUtil.getMovementYaw()) * 0.6;
                    }else if (mc.player.ridingEntity instanceof EntityBoat){
                        motionX = -Math.sin(EntityUtil.getMovementYaw()) * 0.2;
                        motionZ = Math.cos(EntityUtil.getMovementYaw()) * 0.2;
                    }else{
                        motionX = -Math.sin(EntityUtil.getMovementYaw()) * 1.2;
                        motionZ = Math.cos(EntityUtil.getMovementYaw()) * 1.2;
                    }
                }

                if ((mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)
                        && mc.world.getChunk((int)(mc.player.ridingEntity.posX + motionX), (int)(mc.player.ridingEntity.posZ + motionZ)) instanceof EmptyChunk) {
                    mc.player.ridingEntity.motionX = motionX;
                    mc.player.ridingEntity.motionZ = motionZ;
                }
            }
        }
    }
}
