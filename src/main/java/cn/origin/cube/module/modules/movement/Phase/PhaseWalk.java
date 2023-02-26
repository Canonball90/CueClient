package cn.origin.cube.module.modules.movement.Phase;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.player.ProcessRightClickBlockEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.*;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.core.events.event.event.ParallelListener;
import cn.origin.cube.core.events.event.event.Priority;
import cn.origin.cube.utils.player.BlockUtil;
import cn.origin.cube.utils.player.MovementUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@Constant(constant = true)
@ParallelListener(priority = Priority.HIGHEST)
@ModuleInfo(name = "PhaseWalk", descriptions = "", category = Category.MOVEMENT)
public class PhaseWalk extends Module {

    ModeSetting<NoClipMode> noClipMode = registerSetting("Mode", NoClipMode.NoClip);
    BooleanSetting ccbypass = this.registerSetting("CCBypass", false);
    BooleanSetting fallPacket = this.registerSetting("FallPacket", true);
    BooleanSetting instantWalk = this.registerSetting("InstantWalk", true);
    DoubleSetting instantWalkSpeed = this.registerSetting("InstantWalkSpeed", 18.0, 10.0, 19.0).booleanVisible(instantWalk);
    BooleanSetting pearlBypass = this.registerSetting("PearlBypass", true);
    FloatSetting pitch = this.registerSetting("Pitch", 45F, -90F, 90F).booleanVisible(pearlBypass);
    DoubleSetting phaseSpeed = this.registerSetting("PhaseSpeed", 4.24, 1.0, 7.0);
    IntegerSetting antiVoidHeight = this.registerSetting("PhaseSpeed", 5, 1, 100);
    BooleanSetting phaseCheck = this.registerSetting("PhaseCheck", true);
    BooleanSetting downOnShift = this.registerSetting("DownOnShift", true);
    BooleanSetting antiVoid = this.registerSetting("DownOnShift", true);
    BooleanSetting antiKick = this.registerSetting("AntiKick", true);
    BooleanSetting sprintPacket = this.registerSetting("AntiKick", true);
    BooleanSetting clip = this.registerSetting("AntiKick", true);
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
        RayTraceResult trace;
        ++this.delay;
        double phaseSpeedValue = this.phaseSpeed.getValue() / 1000.0;
        double instantSpeedValue = this.instantWalkSpeed.getValue() / 10.0;
        if (this.antiVoid.getValue().booleanValue() && PhaseWalk.mc.player.posY <= (double) this.antiVoidHeight.getValue().intValue() && ((trace = PhaseWalk.mc.world.rayTraceBlocks(PhaseWalk.mc.player.getPositionVector(), new Vec3d(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ), false, false, false)) == null || trace.typeOfHit != RayTraceResult.Type.BLOCK)) {
            PhaseWalk.mc.player.setVelocity(0.0, 0.0, 0.0);
        }
        if (this.phaseCheck.getValue().booleanValue()) {
            if ((PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) && (!this.eChestCheck() && !PhaseWalk.mc.world.getBlockState(getPlayerPos()).getBlock().equals(Blocks.AIR) || !PhaseWalk.mc.world.getBlockState(getPlayerPos().up()).getBlock().equals(Blocks.AIR))) {
                double[] speed;
                double[] dirSpeed;
                if (PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isPressed() && PhaseWalk.mc.player.isSneaking()) {
                    dirSpeed = this.getMotion(phaseSpeedValue);
                    if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.0424, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                    } else {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                    }
                    if (this.noClipMode.getValue() == NoClipMode.Fall) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX, -1300.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.rotationYaw * -5.0f, PhaseWalk.mc.player.rotationPitch * -5.0f, true));
                    }
                    if (this.noClipMode.getValue() == NoClipMode.NoClip) {
                        PhaseWalk.mc.player.setVelocity(0.0, 0.0, 0.0);
                        if (PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown()) {
                            speed = MathUtil.directionSpeed(0.06f);
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX + speed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + speed[1], PhaseWalk.mc.player.onGround));
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        }
                        if (PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY - (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        }
                        if (PhaseWalk.mc.gameSettings.keyBindJump.isKeyDown()) {
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY + (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        }
                    }
                    if (this.noClipMode.getValue() == NoClipMode.Bypass) {
                        PhaseWalk.mc.player.noClip = true;
                    }
                    if (this.fallPacket.getValue().booleanValue()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                    }
                    if (this.sprintPacket.getValue()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    }
                    if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.0424, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                    } else {
                        PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                    }
                    PhaseWalk.mc.player.motionZ = 0.0;
                    PhaseWalk.mc.player.motionY = 0.0;
                    PhaseWalk.mc.player.motionX = 0.0;
                    PhaseWalk.mc.player.noClip = true;
                }
                if (!PhaseWalk.mc.player.collidedHorizontally || !this.clip.getValue() || PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown()) {
                    // empty if block
                }
                if (PhaseWalk.mc.player.collidedHorizontally && this.stopMotion.getValue() ? this.delay >= this.stopMotionDelay.getValue() : PhaseWalk.mc.player.collidedHorizontally) {
                    dirSpeed = this.getMotion(phaseSpeedValue);
                    if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.1, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                    } else {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                    }
                    if (this.noClipMode.getValue() == NoClipMode.Fall) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX, -1300.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.rotationYaw * -5.0f, PhaseWalk.mc.player.rotationPitch * -5.0f, true));
                    }
                    if (this.noClipMode.getValue() == NoClipMode.NoClip) {
                        PhaseWalk.mc.player.setVelocity(0.0, 0.0, 0.0);
                        if (PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown()) {
                            speed = MathUtil.directionSpeed(0.06f);
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX + speed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + speed[1], PhaseWalk.mc.player.onGround));
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        }
                        if (PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY - (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        }
                        if (PhaseWalk.mc.gameSettings.keyBindJump.isKeyDown()) {
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY + (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                            PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        }
                    }
                    if (this.noClipMode.getValue() == NoClipMode.Bypass) {
                        PhaseWalk.mc.player.noClip = true;
                    }
                    if (this.fallPacket.getValue().booleanValue()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                    }
                    if (this.sprintPacket.getValue().booleanValue()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                    }
                    if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.1, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                    } else {
                        PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                    }
                    PhaseWalk.mc.player.motionZ = 0.0;
                    PhaseWalk.mc.player.motionY = 0.0;
                    PhaseWalk.mc.player.motionX = 0.0;
                    PhaseWalk.mc.player.noClip = true;
                    this.delay = 0;
                } else if (this.instantWalk.getValue().booleanValue()) {
                    double[] dir = MathUtil.directionSpeed(instantSpeedValue);
                    PhaseWalk.mc.player.motionX = dir[0];
                    PhaseWalk.mc.player.motionZ = dir[1];
                }
            }
        } else if (PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
            double[] speed;
            double[] dirSpeed;
            if (PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isPressed() && PhaseWalk.mc.player.isSneaking()) {
                dirSpeed = this.getMotion(phaseSpeedValue);
                if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.0424, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                } else {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                }
                if (this.noClipMode.getValue() == NoClipMode.Fall) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX, -1300.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.rotationYaw * -5.0f, PhaseWalk.mc.player.rotationPitch * -5.0f, true));
                }
                if (this.noClipMode.getValue() == NoClipMode.NoClip) {
                    PhaseWalk.mc.player.setVelocity(0.0, 0.0, 0.0);
                    if (PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown()) {
                        speed = MathUtil.directionSpeed(0.06f);
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX + speed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + speed[1], PhaseWalk.mc.player.onGround));
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                    }
                    if (PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY - (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                    }
                    if (PhaseWalk.mc.gameSettings.keyBindJump.isKeyDown()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY + (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                    }
                }
                if (this.noClipMode.getValue() == NoClipMode.Bypass) {
                    PhaseWalk.mc.player.noClip = true;
                }
                if (this.fallPacket.getValue().booleanValue()) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (this.sprintPacket.getValue().booleanValue()) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }
                if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
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
                dirSpeed = this.getMotion(phaseSpeedValue);
                if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.1, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                } else {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1], PhaseWalk.mc.player.rotationYaw, PhaseWalk.mc.player.rotationPitch, false));
                }
                if (this.noClipMode.getValue() == NoClipMode.Fall) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(PhaseWalk.mc.player.posX, -1300.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.rotationYaw * -5.0f, PhaseWalk.mc.player.rotationPitch * -5.0f, true));
                }
                if (this.noClipMode.getValue() == NoClipMode.NoClip) {
                    PhaseWalk.mc.player.setVelocity(0.0, 0.0, 0.0);
                    if (PhaseWalk.mc.gameSettings.keyBindForward.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindBack.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindLeft.isKeyDown() || PhaseWalk.mc.gameSettings.keyBindRight.isKeyDown()) {
                        speed = MathUtil.directionSpeed(0.06f);
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX + speed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + speed[1], PhaseWalk.mc.player.onGround));
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                    }
                    if (PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY - (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                    }
                    if (PhaseWalk.mc.gameSettings.keyBindJump.isKeyDown()) {
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, PhaseWalk.mc.player.posY + (double) 0.06f, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                        PhaseWalk.mc.player.connection.sendPacket(new CPacketPlayer.Position(PhaseWalk.mc.player.posX, 0.0, PhaseWalk.mc.player.posZ, PhaseWalk.mc.player.onGround));
                    }
                }
                if (this.noClipMode.getValue() == NoClipMode.Bypass) {
                    PhaseWalk.mc.player.noClip = true;
                }
                if (this.fallPacket.getValue().booleanValue()) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.STOP_RIDING_JUMP));
                }
                if (this.sprintPacket.getValue().booleanValue()) {
                    PhaseWalk.mc.player.connection.sendPacket(new CPacketEntityAction(PhaseWalk.mc.player, CPacketEntityAction.Action.START_SPRINTING));
                }
                if (this.downOnShift.getValue().booleanValue() && PhaseWalk.mc.player.collidedVertically && PhaseWalk.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY - 0.1, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                } else {
                    PhaseWalk.mc.player.setPosition(PhaseWalk.mc.player.posX + dirSpeed[0], PhaseWalk.mc.player.posY, PhaseWalk.mc.player.posZ + dirSpeed[1]);
                }
                PhaseWalk.mc.player.motionZ = 0.0;
                PhaseWalk.mc.player.motionY = 0.0;
                PhaseWalk.mc.player.motionX = 0.0;
                PhaseWalk.mc.player.noClip = true;
                this.delay = 0;
            } else if (this.instantWalk.getValue().booleanValue()) {
                double[] dir = MathUtil.directionSpeed(instantSpeedValue);
                PhaseWalk.mc.player.motionX = dir[0];
                PhaseWalk.mc.player.motionZ = dir[1];
            }
        }
    }

    private double[] getMotion(double speed) {
        float moveForward = PhaseWalk.mc.player.movementInput.moveForward;
        float moveStrafe = PhaseWalk.mc.player.movementInput.moveStrafe;
        float rotationYaw = PhaseWalk.mc.player.prevRotationYaw + (PhaseWalk.mc.player.rotationYaw - PhaseWalk.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? -45 : 45);
            } else if (moveStrafe < 0.0f) {
                rotationYaw += (float) (moveForward > 0.0f ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            } else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        double posX = (double) moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + (double) moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        double posZ = (double) moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - (double) moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[]{posX, posZ};
    }

    private double[] getDistance() {
        float forward = PhaseWalk.mc.player.movementInput.moveForward;
        float strafe = PhaseWalk.mc.player.movementInput.moveStrafe;
        float rotYaw = PhaseWalk.mc.player.prevRotationYaw + (PhaseWalk.mc.player.rotationYaw - PhaseWalk.mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        double posX = (double) strafe * (double) forward * -Math.sin(Math.toRadians(rotYaw)) + (double) strafe * (double) forward * Math.cos(Math.toRadians(rotYaw));
        double posZ = (double) strafe * (double) forward * Math.cos(Math.toRadians(rotYaw)) - (double) strafe * (double) forward * -Math.sin(Math.toRadians(rotYaw));
        return new double[]{posX, posZ};
    }

    @Override
    public void onDisable() {
        PhaseWalk.mc.player.noClip = false;
    }

    private boolean eChestCheck() {
        String loc = String.valueOf(PhaseWalk.mc.player.posY);
        String deciaml = loc.split("\\.")[1];
        return deciaml.equals("875");
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public enum NoClipMode {
        NoClip,
        Fall,
        Bypass,
        None
    }
}
