package cn.origin.cube.module.modules.movement.Phase;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.player.ProcessRightClickBlockEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.*;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.client.event.event.ParallelListener;
import cn.origin.cube.utils.client.event.event.Priority;
import cn.origin.cube.utils.player.BlockUtil;
import cn.origin.cube.utils.player.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Constant(constant = true)
@ParallelListener(priority = Priority.HIGHEST)
@ModuleInfo(name = "PhaseWalk", descriptions = "", category = Category.MOVEMENT)
public class PhaseWalk extends Module {

    BooleanSetting fallPacket = this.registerSetting("FallPacket", true);
    BooleanSetting instantWalk = this.registerSetting("InstantWalk", true);
    DoubleSetting instantWalkSpeed = this.registerSetting("InstantWalkSpeed", 18.0, 10.0, 19.0).booleanVisible(instantWalk);
    BooleanSetting pearlBypass = this.registerSetting("PearlBypass", true);
    FloatSetting pitch = this.registerSetting("Pitch", 45F, -90F, 90F).booleanVisible(pearlBypass);
    DoubleSetting phaseSpeed = this.registerSetting("PhaseSpeed", 4.24, 1.0, 7.0);
    BooleanSetting phaseCheck = this.registerSetting("PhaseCheck", true);
    BooleanSetting downOnShift = this.registerSetting("DownOnShift", true);
    BooleanSetting antiKick = this.registerSetting("AntiKick", true);
    BooleanSetting stopMotion = this.registerSetting("StopMotion", true);
    IntegerSetting stopMotionDelay = this.registerSetting("StopMotionDelay", 5, 0, 20).booleanVisible(stopMotion);
    BooleanSetting blockFly = this.registerSetting("BlockFly", false);
    BooleanSetting hotkey = this.registerSetting("Hotkey", false).booleanVisible(blockFly);
    BindSetting key = this.registerSetting("Key", new BindSetting.KeyBind(0)).booleanVisible(blockFly).booleanVisible(hotkey);
    boolean doAntiKick = false;
    int delay = 0;
    static int c;

    @Override
    public void onUpdate() {
        ++this.delay;
        int loops = (int) Math.floor(2);
        double motionY = 0.0;
        if ((PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) && (this.phaseCheck.getValue() && !PhaseUtils.eChestCheck() && !PhaseWalk.mc.world.getBlockState(BlockUtil.getPlayerPos()).getBlock().equals((Object)Blocks.AIR) || !PhaseWalk.mc.world.getBlockState(BlockUtil.getPlayerPos().up()).getBlock().equals((Object)Blocks.AIR))) {
            double[] dirSpeed;
            if (PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isPressed() && PhaseWalk.mc.player.isSneaking()) {
                dirSpeed = PhaseUtils.getMotion(this.phaseSpeed.getValue() / 100.0);
                if (this.downOnShift.getValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.0424, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                } else {
                    PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                }
                PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX, -1337.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.rotationYaw * -5.0f, PhaseWalk.mc.player.rotationPitch * -5.0f, true));
                if (this.fallPacket.getValue()) {
                    PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (this.downOnShift.getValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.0424, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                } else {
                    PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                }
                PhaseWalk.mc.player.motionZ = 0.0;
                PhaseWalk.mc.player.motionY = 0.0;
                PhaseWalk.mc.player.motionX = 0.0;
                PhaseWalk.mc.player.noClip = true;
            }
            if (PhaseWalk.mc.player.collidedHorizontally && this.stopMotion.getValue() ? this.delay >= this.stopMotionDelay.getValue() : PhaseWalk.mc.player.collidedHorizontally) {
                dirSpeed = PhaseUtils.getMotion(this.phaseSpeed.getValue() / 100.0);
                if (this.downOnShift.getValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.1, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                } else {
                    PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                }
                PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX, -1337.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.rotationYaw * -5.0f, PhaseWalk.mc.player.rotationPitch * -5.0f, true));
                doAntiKick = antiKick.getValue()
                        && mc.player.ticksExisted % 40 == 0
                        && !PhaseUtils.isPhased()
                        && !mc.world.collidesWithAnyBlock(mc.player.getEntityBoundingBox())
                        && !MovementUtils.isMoving(mc.player);

                if (doAntiKick) {
                    loops = 1;
                    motionY = -0.04;
                }
                if (this.fallPacket.getValue()) {
                    PhaseWalk.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)PhaseWalk.mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (this.downOnShift.getValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.1, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                } else {
                    PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                }
                PhaseWalk.mc.player.motionZ = 0.0;
                PhaseWalk.mc.player.motionY = 0.0;
                PhaseWalk.mc.player.motionX = 0.0;
                PhaseWalk.mc.player.noClip = true;
                this.delay = 0;
            } else if (this.instantWalk.getValue()) {
                double[] dir = MathUtil.directionSpeed(this.instantWalkSpeed.getValue() / 100.0);
                PhaseWalk.mc.player.motionX = dir[0];
                PhaseWalk.mc.player.motionZ = dir[1];
            }
        }

        if(blockFly.getValue()){
            if(constant = true) {
                if(hotkey.getValue()) {
                    if (Keyboard.isKeyDown(this.key.getValue().getKeyCode())) {
                        PhaseUtils.doFly();
                    }
                }else{
                    PhaseUtils.doFly();
                }
            }
        }

        if(!PhaseUtils.isPhased()){
            toggle();
        }
    }

    @Override
    public void onLogout() {
        toggle();
        super.onLogout();
    }

    @Override
    public void onEnable() {
        c = 0;

        mc.player.rotationPitch = pitch.getValue();
        mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
    }

    @SubscribeEvent
    public void onRightClickBlockEvent (ProcessRightClickBlockEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if(pearlBypass.getValue()) {
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() == Items.ENDER_PEARL) {
                mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItem(event.hand));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            event.setCanceled(true);
        }
    }

    @Override
    public void onDisable() {
        PhaseWalk.mc.player.noClip = false;
    }

}
