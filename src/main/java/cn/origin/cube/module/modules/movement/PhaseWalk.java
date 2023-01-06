package cn.origin.cube.module.modules.movement;

import cn.origin.cube.core.events.player.ProcessRightClickBlockEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.DoubleSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.BlockUtil;
import cn.origin.cube.utils.player.InventoryUtil;
import cn.origin.cube.utils.player.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

@ModuleInfo(name = "PhaseWalk", descriptions = "", category = Category.MOVEMENT)
public class PhaseWalk extends Module {

    BooleanSetting fallPacket = registerSetting("FallPacket", true);
    BooleanSetting instantWalk = registerSetting("InstantWalk", true);
    DoubleSetting instantWalkSpeed = registerSetting("InstantWalkSpeed", 18.0, 10.0, 19.0).booleanVisible(instantWalk);
    BooleanSetting pearlBypass = registerSetting("PearlBypass", true);
    FloatSetting pitch = registerSetting("Pitch", 45F, -90F, 90F).booleanVisible(pearlBypass);
    DoubleSetting phaseSpeed = registerSetting("PhaseSpeed", 4.24, 1.0, 7.0);
    BooleanSetting phaseCheck = registerSetting("PhaseCheck", true);
    BooleanSetting downOnShift = registerSetting("DownOnShift", true);
    BooleanSetting antiKick = registerSetting("AntiKick", true);
    BooleanSetting silent = registerSetting("Silent", false);
    BooleanSetting stopMotion = registerSetting("StopMotion", true);
    IntegerSetting stopMotionDelay = registerSetting("StopMotionDelay", 5, 0, 20).booleanVisible(stopMotion);
    boolean doAntiKick = false;
    int delay = 0;

    @Override
    public void onUpdate() {
        if(fullNullCheck()) toggle();
        ++this.delay;
        int loops = (int) Math.floor(2);
        double motionY = 0.0;
        if ((mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) && (this.phaseCheck.getValue() && !this.eChestCheck() && !mc.world.getBlockState(BlockUtil.getPlayerPos()).getBlock().equals((Object)Blocks.AIR) || !mc.world.getBlockState(BlockUtil.getPlayerPos().up()).getBlock().equals((Object)Blocks.AIR))) {
            double[] dirSpeed;
            if (mc.player.collidedVertically && mc.gameSettings.keyBindSneak.isPressed() && mc.player.isSneaking()) {
                dirSpeed = this.getMotion(this.phaseSpeed.getValue() / 100.0);
                if (this.downOnShift.getValue() && mc.player.collidedVertically && mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX + dirSpeed[0], mc.player.posY - 0.0424, mc.player.posZ + dirSpeed[1], mc.player.rotationYaw, mc.player.rotationPitch, false));
                } else {
                    mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX + dirSpeed[0], mc.player.posY, mc.player.posZ + dirSpeed[1], mc.player.rotationYaw, mc.player.rotationPitch, false));
                }
                mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX, -1337.0, mc.player.posZ, mc.player.rotationYaw * -5.0f, mc.player.rotationPitch * -5.0f, true));
                if (this.fallPacket.getValue()) {
                    mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (this.downOnShift.getValue() && mc.player.collidedVertically && mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.setPosition(mc.player.posX + dirSpeed[0], mc.player.posY - 0.0424, mc.player.posZ + dirSpeed[1]);
                } else {
                    mc.player.setPosition(mc.player.posX + dirSpeed[0], mc.player.posY, mc.player.posZ + dirSpeed[1]);
                }
                mc.player.motionZ = 0.0;
                mc.player.motionY = 0.0;
                mc.player.motionX = 0.0;
                mc.player.noClip = true;
            }
            if (mc.player.collidedHorizontally && this.stopMotion.getValue() ? this.delay >= this.stopMotionDelay.getValue() : mc.player.collidedHorizontally) {
                dirSpeed = this.getMotion(this.phaseSpeed.getValue() / 100.0);
                if (this.downOnShift.getValue() && mc.player.collidedVertically && mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX + dirSpeed[0], mc.player.posY - 0.1, mc.player.posZ + dirSpeed[1], mc.player.rotationYaw, mc.player.rotationPitch, false));
                } else {
                    mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX + dirSpeed[0], mc.player.posY, mc.player.posZ + dirSpeed[1], mc.player.rotationYaw, mc.player.rotationPitch, false));
                }
                mc.player.connection.sendPacket((Packet)new CPacketPlayer.PositionRotation(mc.player.posX, -1337.0, mc.player.posZ, mc.player.rotationYaw * -5.0f, mc.player.rotationPitch * -5.0f, true));
                doAntiKick = antiKick.getValue()
                        && mc.player.ticksExisted % 40 == 0
                        && !isPhased()
                        && !mc.world.collidesWithAnyBlock(mc.player.getEntityBoundingBox())
                        && !MovementUtils.isMoving(mc.player);

                if (doAntiKick) {
                    loops = 1;
                    motionY = -0.04;
                }
                if (this.fallPacket.getValue()) {
                    mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (this.downOnShift.getValue() && mc.player.collidedVertically && mc.gameSettings.keyBindSneak.isKeyDown()) {
                    mc.player.setPosition(mc.player.posX + dirSpeed[0], mc.player.posY - 0.1, mc.player.posZ + dirSpeed[1]);
                } else {
                    mc.player.setPosition(mc.player.posX + dirSpeed[0], mc.player.posY, mc.player.posZ + dirSpeed[1]);
                }
                mc.player.motionZ = 0.0;
                mc.player.motionY = 0.0;
                mc.player.motionX = 0.0;
                mc.player.noClip = true;
                this.delay = 0;
            } else if (this.instantWalk.getValue()) {
                double[] dir = MathUtil.directionSpeed(this.instantWalkSpeed.getValue() / 100.0);
                mc.player.motionX = dir[0];
                mc.player.motionZ = dir[1];
            }
        }

        if(!isPhased()) toggle();
    }

    @Override
    public void onEnable() {
        if(fullNullCheck()) toggle();
        mc.player.rotationPitch = pitch.getValue();
        if(!silent.getValue()) {
            mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
        }else {
            int oldSlot = mc.player.inventory.currentItem;
            int p = InventoryUtil.findHotbarItem(ItemEnderPearl.class);
            if (p == -1) {
                return;
            }
            InventoryUtil.switchToHotbarSlot(p, false);
            try {
                mc.playerController.processRightClick(mc.player, mc.world, (mc.player.getHeldItemOffhand().item == Items.ENDER_PEARL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItem((mc.player.getHeldItemOffhand().item == Items.ENDER_PEARL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
            } catch (Exception ignored) {
            }
            InventoryUtil.switchToHotbarSlot(oldSlot, false);
        }
    }

    private double[] getMotion(double speed) {
        float moveForward = mc.player.movementInput.moveForward;
        float moveStrafe = mc.player.movementInput.moveStrafe;
        float rotationYaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float)(moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double)moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double)moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double)moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double)moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    @SubscribeEvent
    public void onRightClickBlockEvent (ProcessRightClickBlockEvent event) {
        if (fullNullCheck())return;
        if(pearlBypass.getValue()) {
            if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).getItem() == Items.ENDER_PEARL) {
                mc.player.connection.sendPacket((Packet) new CPacketPlayerTryUseItem(event.hand));
                event.setCanceled(true);
            }
        }
    }

    private boolean isPhased() {
        return !mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().expand(-0.0625, -0.0625, -0.0625)).isEmpty();
    }

    @Override
    public void onDisable() {
        mc.player.noClip = false;
    }

    private boolean eChestCheck() {
        String loc = String.valueOf(mc.player.posY);
        String deciaml = loc.split("\\.")[1];
        return deciaml.equals("875");
    }
}
