package cn.origin.cube.module.modules.combat;

import cn.origin.cube.Cube;
import cn.origin.cube.event.events.player.UpdateWalkingPlayerEvent;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.DoubleSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.module.interfaces.Para;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.InventoryUtil;
import cn.origin.cube.utils.player.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Date;

@Para(para = Para.ParaMode.Light)
@ModuleInfo(name = "KillAura", descriptions = "Auto attack entity", category = Category.COMBAT)
public class KillAura extends Module {
    long killLast = new Date().getTime();
    public boolean shouldRotate;
    public float yaw;
    public float pitch;
    public boolean shouldReset;
    public float playerPitch;
    public float lastPlayerPitch;
    public float playerYaw;
    public float renderYaw;
    public float YawOffset;
    float rotationYaw;
    boolean smoothRotatePitch;
    boolean smoothRotated;
    boolean smoothRotateYaw;
    int addedInputYaw;
    float smoothYaw;
    float rotationPitch;
    int addedOriginYaw;
    float smoothPitch;
    DoubleSetting hittingRange = registerSetting("Range", 5.5, 0.1, 10.0);
    IntegerSetting delay = registerSetting("Delay", 4, 0, 70);
    BooleanSetting randomD = registerSetting("RandomDelay", false);
    IntegerSetting randomDelay = registerSetting("Random Delay", 4, 0, 40).booleanVisible(randomD);
    IntegerSetting iterations = registerSetting("Iterations", 1, 1, 10);
    DoubleSetting wallsRange = registerSetting("Wall Range", 3.5, 1, 10);
    IntegerSetting ticksExisted = registerSetting("TicksExisted", 20, 0, 150);
    BooleanSetting hitDelay = registerSetting("HitDelay", true);
    BooleanSetting packet = registerSetting("PacketHit", false);
    BooleanSetting swimArm = registerSetting("SwimArm", true).booleanVisible(packet);
    BooleanSetting rotate = registerSetting("Rotate", true);
    BooleanSetting rotateStrict = registerSetting("StrictRotate", true);
    BooleanSetting throughWalls = registerSetting("ThroughWalls", true);

    BooleanSetting playerOnly = registerSetting("PlayerOnly", true);
    BooleanSetting weaponOnly = registerSetting("WeaponOnly", true);
    BooleanSetting switchWeapon = registerSetting("SwitchWeapon", false);
    ModeSetting<currentW> weaponCurrent = registerSetting("CurrentWeapon", currentW.NONE).boolean2NVisible(switchWeapon, weaponOnly);

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        boolean isReadyAttack = mc.player.getCooledAttackStrength(0.0f) >= 1;
        if (hitDelay.getValue()) {
            if (!isReadyAttack) return;
        }
        mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase)
                .filter(entity -> mc.player.getDistance(entity) <= hittingRange.getValue())
                .filter(entity -> !Cube.friendManager.isFriend(entity.getName()))
                .filter(entity -> entity != mc.player)
                .forEach(target -> {
                    if (playerOnly.getValue()) {
                        if (!(target instanceof EntityPlayer) ||
                                target.ticksExisted >= this.ticksExisted.getValue() &&
                                        mc.player.getDistance(target) <= this.hittingRange.getValue() &&
                                        (mc.player.canEntityBeSeen(target) || !this.throughWalls.getValue() ||
                                                mc.player.getDistance(target) <= this.wallsRange.getValue()))return;
                    }
                    if (switchWeapon.getValue()) {
                        int currentSlot;
                        if (currentW.SWORD.equals(weaponCurrent.getValue())) {
                            if ((currentSlot = InventoryUtil.findHotbarItem(ItemSword.class)) != -1) {
                                InventoryUtil.switchToHotbarSlot(currentSlot, false);
                            }
                        } else if (currentW.AXE.equals(weaponCurrent.getValue())) {
                            if ((currentSlot = InventoryUtil.findHotbarItem(ItemAxe.class)) != -1) {
                                InventoryUtil.switchToHotbarSlot(currentSlot, false);
                            }
                        }
                    }
                    if (!weaponCurrent.getValue().equals(currentW.NONE)) {
                        if (mc.player.getHeldItemMainhand() == ItemStack.EMPTY
                                || !(weaponCurrent.getValue().equals(currentW.AXE) ? ItemAxe.class : ItemSword.class)
                                .isInstance(mc.player.getHeldItemMainhand().getItem())) {
                            return;
                        }
                    }
                    final int delay = (int)(this.delay.getValue() * 10 + (randomD.getValue() ? this.randomDelay.getValue() * 10 * Math.random() : 0));
                    if (new Date().getTime() >= this.killLast + delay) {
                        this.killLast = new Date().getTime();
                        if (rotate.getValue()) {
                            if(rotateStrict.getValue()) {
                                rotateTo(target.posX, target.posY, target.posZ, mc.player, false);
                            }else{
                                rotateTo(target);
                            }
                        }
                        for (int i = 0; i < this.iterations.getValue(); ++i) {
                            attack(target);
                        }
                    }
                });

    }
    //ToDo do the rendering shit

//    @Override
//    public void onRender3D(Render3DEvent event) {
//        mc.world.loadedEntityList.stream()
//                .filter(entity -> entity instanceof EntityLivingBase)
//                .filter(entity -> mc.player.getDistance(entity) <= hittingRange.getValue())
//                .filter(entity -> !Cube.friendManager.isFriend(entity.getName()))
//                .filter(entity -> entity != mc.player)
//                .forEach(target -> {
//                    if (playerOnly.getValue()) {
//                        if (!(target instanceof EntityPlayer) ||
//                                target.ticksExisted >= this.ticksExisted.getValue() &&
//                                        mc.player.getDistance(target) <= this.hittingRange.getValue() &&
//                                        (mc.player.canEntityBeSeen(target) || !this.throughWalls.getValue() ||
//                                                mc.player.getDistance(target) <= this.wallsRange.getValue()))return;
//                    }
//                    Render3DUtil.drawBBBox(target.getCollisionBoundingBox(), ClickGui.getCurrentColor(), 100, 5, true);
//                });
//        super.onRender3D(event);
//    }

    public void attack(Entity entity) {
            rotateTo(entity.posX, entity.posY, entity.posZ, mc.player, false);
                if (hitDelay.getValue()) {
                    if(!packet.getValue()) {
                        if (mc.player.getCooledAttackStrength(0) >= 1) {
                            mc.playerController.attackEntity(mc.player, entity);
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    }else{
                        mc.playerController.connection.sendPacket(new CPacketUseEntity(entity));
                        if (swimArm.getValue()) mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
                if (!hitDelay.getValue()) {
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
    }

    public void rotateTo(Entity target) {
        RotationUtil.faceVector(new Vec3d(target.posX, target.posY + 1, target.posZ), true);
    }

    public boolean rotateTo(final double n, final double n2, final double n3, final EntityPlayer entityPlayer, final boolean b){
        final double[] calculateLook = EntityUtil.calculateLookAt(n, n2, n3, entityPlayer);
        return setRotation((float) calculateLook[0], (float) calculateLook[1], b);
    }

    public boolean setRotation(float setSmoothRotationYaw, float n, final boolean b) {
        final boolean b2 = false;
        smoothRotatePitch = b2;
        smoothRotateYaw = b2;
        smoothRotated = true;
        if (b) {
            if (!shouldRotate) {
                yaw = mc.player.prevRotationYaw;
                pitch = mc.player.prevRotationPitch;
            }
            if (calculateDirectionDifference(setSmoothRotationYaw + 180.0f, yaw + 180.0f) > 90.0) {
                setSmoothRotationYaw = setSmoothRotationYaw(setSmoothRotationYaw, yaw);
                smoothRotated = false;
            }
            if (Math.abs(n - pitch) > 90.0f) {
                smoothRotatePitch = true;
                smoothRotated = false;
                smoothPitch = n;
                if (n > pitch) {
                    n -= (n - pitch) / 2.0f;
                } else {
                    n += (pitch - n) / 2.0f;
                }
            }
        }
        yaw = setSmoothRotationYaw;
        pitch = n;
        shouldRotate = true;
        return !smoothRotatePitch && !smoothRotateYaw;
    }

    public float setSmoothRotationYaw(float smoothYaw, float n) {
        smoothRotateYaw = true;
        final int n2 = 0;
        addedOriginYaw = n2;
        addedInputYaw = n2;
        while (smoothYaw + 180.0f < 0.0f) {
            smoothYaw += 360.0f;
            ++addedInputYaw;
        }
        while (smoothYaw + 180.0f > 360.0f) {
            smoothYaw -= 360.0f;
            --addedInputYaw;
        }
        while (n + 180.0f < 0.0f) {
            n += 360.0f;
            ++addedOriginYaw;
        }
        while (n + 180.0f > 360.0f) {
            n -= 360.0f;
            --addedOriginYaw;
        }
        smoothYaw += 180.0f;
        n += 180.0f;
        final double n3 = n - smoothYaw;
        if (n3 >= -180.0 && n3 >= 180.0) {
            smoothYaw -= (float) (n3 / 2.0);
        } else {
            smoothYaw += (float) (n3 / 2.0);
        }
        smoothYaw -= 180.0f;
        if (addedInputYaw > 0) {
            for (int i = 0; i < addedInputYaw; ++i) {
                smoothYaw -= 360.0f;
            }
        } else if (addedInputYaw < 0) {
            for (int j = 0; j > addedInputYaw; --j) {
                smoothYaw += 360.0f;
            }
        }
        return smoothYaw;
    }

    public static double calculateDirectionDifference(double n, double n2) {
        while (n < 0.0) {
            n += 360.0;
        }
        while (n > 360.0) {
            n -= 360.0;
        }
        while (n2 < 0.0) {
            n2 += 360.0;
        }
        while (n2 > 360.0) {
            n2 -= 360.0;
        }
        final double n3 = Math.abs(n2 - n) % 360.0;
        return (n3 > 180.0) ? (360.0 - n3) : n3;
    }

    enum currentW {
        NONE,
        SWORD,
        AXE
    }

}