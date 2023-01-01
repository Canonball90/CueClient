package cn.origin.cube.module.modules.combat.newAutoCrystal;

import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.interfaces.Para;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.player.ai.AI;
import cn.origin.cube.utils.player.ai.AIUtils;
import cn.origin.cube.utils.player.ai.CrystalUtils;
import cn.origin.cube.utils.player.ai.InventoryUtil;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.client.event.event.ParallelListener;
import cn.origin.cube.utils.client.event.event.Priority;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.RotationUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;

@Para(para = Para.ParaMode.Full)
@ParallelListener(priority = Priority.HIGHEST)
@ModuleInfo(name = "NewAutoCrystal", descriptions = "", category = Category.COMBAT)
public class AutoCrystal extends Module {
    public static AutoCrystal instance = new AutoCrystal();

    ModeSetting<CaLogic> logic = registerSetting("CaLogic", CaLogic.BreakPlace);
    IntegerSetting placeRange = registerSetting("PlaceRange", 4, 1, 6);
    FloatSetting placeWallRange = registerSetting("PlaceWallRange", 4.5f, 0, 6);
    ModeSetting<Hand> hand = registerSetting("Hand", Hand.OffHand);
    IntegerSetting breakRange = registerSetting("BreakRange", 4,1,6);
    IntegerSetting targetRange = registerSetting("TargetRange", 15,1,20);
    BooleanSetting packetBreak = registerSetting("PacketBreak", false);
    BooleanSetting packetPlace = registerSetting("PacketPlace", false);
    IntegerSetting minDMG = registerSetting("MinDMG", 6,0,37);
    IntegerSetting maxSelfDMG = registerSetting("MaxSelfDMG", 18,1,80);
    BooleanSetting antiSuicide = registerSetting("AntiSuicide", false);
    BooleanSetting multiPlace = registerSetting("MultiPlace", false);
    BooleanSetting raytrace = registerSetting("RayTrace", true);
    BooleanSetting check = registerSetting("Check", true);
    IntegerSetting placeDelay = registerSetting("PlaceDelay", 4,0,80);
    IntegerSetting breakDelay = registerSetting("BreakDelay", 4,0,80);
    IntegerSetting lethalMult = registerSetting("lethalMult", 0,0,6);
    ModeSetting<SwitchMode> switchMode = registerSetting("SwitchMode", SwitchMode.None);
    BooleanSetting render = registerSetting("Render", true);
    BooleanSetting sound = registerSetting("Sound", true);
    BooleanSetting clientSide = registerSetting("ClientSide", true);
    BooleanSetting rotate = registerSetting("Rotate", true);
    ModeSetting<Rotate> rotateMode = registerSetting("RotateMode", Rotate.Packet);
    BooleanSetting MultiThread = registerSetting("MultiThread", true);
    IntegerSetting MultiThreadValue = registerSetting("MultiThreadVal", 2,1,4);
    IntegerSetting MultiThreadDelay = registerSetting("MultiThreadDelay", 0,0,60);

    static AI.HalqPos bestCrystalPos = new AI.HalqPos(BlockPos.ORIGIN, 0);

    public AutoCrystal() {
        instance = this;
    }

    @SubscribeEvent
    public void onRecieve(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect && sound.getValue()) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
                for (Entity e : Minecraft.getMinecraft().world.loadedEntityList)
                    if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0f)
                        e.setDead();
        }
    }

    private final Timer threadDelay = new Timer();

    private final Timer placeTimer = new Timer(), breakTimer = new Timer();
    public EntityPlayer target;
    Thread thread;
    private AutoCrystal autoCrystal;

    @Override
    public void onEnable() {
        placeTimer.reset();
        bestCrystalPos = new AI.HalqPos(BlockPos.ORIGIN, 0);
    }

    public void onDisable() {
        target = null;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (render.getValue() && !bestCrystalPos.getBlockPos().equals(BlockPos.ORIGIN))
            Render3DUtil.drawBlockBox(bestCrystalPos.getBlockPos(), Colors.getGlobalColor(), true, 2f);
    }

    public AI.HalqPos placeCalculateAI() {
        AI.HalqPos posToReturn = new AI.HalqPos(BlockPos.ORIGIN, 0.5f);
        for (BlockPos pos : AIUtils.getSphere(placeRange.getValue())) {
            float targetDamage = CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, target, true);
            float selfDamage = CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player, true);
            if (CrystalUtils.canPlaceCrystal(pos, check.getValue(), true, multiPlace.getValue(), false)) {
                if (mc.player.getDistance(pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f) > MathUtil.square(placeRange.getValue())) continue;
                if (selfDamage > maxSelfDMG.getValue()) continue;
                if (targetDamage < minDMG.getValue()) continue;
                if (antiSuicide.getValue()) if (selfDamage < 2F) continue;
                if (targetDamage > posToReturn.getTargetDamage()) posToReturn = new AI.HalqPos(pos, targetDamage);
            }
        }
        return posToReturn;
    }

    public void doBreak() {
        final EntityEnderCrystal crystal = (EntityEnderCrystal) mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).min(Comparator.comparing(c -> mc.player.getDistance(c))).orElse(null);
        if (crystal == null) return;
        breakProcess(crystal);
        mc.playerController.updateController();
    }

    public void oldRotate(float pitch, float yaw) {
        if (rotate.getValue()) {
            if (rotateMode.getValue().equals(Rotate.Spoof)) {
                mc.player.rotationYaw = yaw;
                mc.player.rotationPitch = pitch;
            } else if (rotateMode.getValue().equals(Rotate.Packet)) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, yaw, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationPitch, pitch, mc.player.onGround));
            }
        }
    }

    public void newRotate(float pitch, float yaw) {
        if (rotate.getValue()) {
            if (rotateMode.getValue().equals(Rotate.Normal)) {
                mc.player.rotationYaw = yaw;
                mc.player.rotationPitch = pitch;
            } else if (rotateMode.getValue().equals(Rotate.Packet)) {
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, yaw, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationPitch, pitch, mc.player.onGround));
            }
        }
    }

    @SubscribeEvent
    public void onSend(PacketEvent.Send event){
        bestCrystalPos = placeCalculateAI();
        float[] rot = RotationUtil.getRotationToPos(bestCrystalPos.getBlockPos());
        Packet packet = event.getPacket();
        if (packet instanceof CPacketPlayer && rotateMode.getValue().equals(Rotate.Spoof)) {
           // ((CPacketPlayer) packet).yaw = rot[0];
           // ((CPacketPlayer) packet).pitch = rot[1];
        }
    }

    private void breakProcess(Entity entity) {
        if (breakTimer.passedDms(breakDelay.getValue())) {
            if (mc.player.getDistance(entity) < breakRange.getValue()) {
                if (breakTimer.passedMs(breakDelay.getValue())) {
                    float[] rot = RotationUtil.getRotationToPos(entity.getPosition());
                    newRotate(rot[1], rot[0]);
                    for (int i = 0; i <= 1; i++) {
                        if (packetBreak.getValue()) {
                            mc.player.connection.sendPacket(new CPacketUseEntity(entity));
                            CPacketUseEntity packet = new CPacketUseEntity();
                            packet.entityId = entity.entityId;
                            packet.action = CPacketUseEntity.Action.ATTACK;
                            mc.player.connection.sendPacket(packet);
                        } else mc.playerController.attackEntity(mc.player, entity);
                    }
                    try {if (clientSide.getValue()) mc.world.removeEntityFromWorld(entity.entityId);} catch (Exception ignored) {}
                    breakTimer.reset();
                }
                breakTimer.reset();
            }
            if (hand.getValue().equals(Hand.MainHand)) mc.player.swingArm(EnumHand.MAIN_HAND);
            else if (hand.getValue().equals(Hand.OffHand)) mc.player.swingArm(EnumHand.OFF_HAND);
            else mc.player.connection.sendPacket(new CPacketAnimation());
            breakTimer.reset();
        }
        breakTimer.reset();
    }

    public void doPlace() {
        bestCrystalPos = placeCalculateAI();

        float[] old = new float[]{mc.player.rotationYaw, mc.player.rotationPitch};
        if (bestCrystalPos.getBlockPos() != BlockPos.ORIGIN) {
            if (placeTimer.passedDms(placeDelay.getValue())) {
                int crystalSlot = InventoryUtil.findItem(Items.END_CRYSTAL, 0, 9), oldSlot = mc.player.inventory.currentItem;
                boolean canSwitch = true, offhand = mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL);
                if (crystalSlot != mc.player.inventory.currentItem && switchMode.getValue().equals(SwitchMode.None) && !offhand) return;
                if (crystalSlot == mc.player.inventory.currentItem || offhand) canSwitch = false;
                if (canSwitch) InventoryUtil.switchToSlot(crystalSlot, switchMode.getValue().equals(SwitchMode.Silent));
                if (placeTimer.passedMs(placeDelay.getValue())) {
                    if (packetPlace.getValue()) mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(bestCrystalPos.getBlockPos(), AI.getEnumFacing(raytrace.getValue(), bestCrystalPos.getBlockPos()), mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                    else mc.playerController.processRightClickBlock(mc.player, mc.world, bestCrystalPos.getBlockPos(), AI.getEnumFacing(raytrace.getValue(), bestCrystalPos.getBlockPos()), new Vec3d(0, 0, 0), mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                    placeTimer.reset();
                }
                placeTimer.reset();
                if (switchMode.getValue().equals(SwitchMode.Silent)) InventoryUtil.switchToSlot(oldSlot, true);
            }
            placeTimer.reset();
        }
        oldRotate(old[1], old[0]);
    }

    public static AutoCrystal get(AutoCrystal autoCrystal) {
        if (instance == null) {
            instance = new AutoCrystal();
            instance.autoCrystal = autoCrystal;
        }
        return instance;
    }

    public enum SwitchMode {None, Normal, Silent}

    public enum CaLogic {BreakPlace, PlaceBreak}

    public enum Hand {MainHand, OffHand, PacketSwing}

    public enum Rotate {Packet, Normal, Spoof}

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;
        target = EntityUtil.getTarget(targetRange.getValue());
        if (logic.getValue().equals(CaLogic.BreakPlace)) {
            doBreak();
            doPlace();
        } else if (logic.getValue().equals(CaLogic.PlaceBreak)) {
            doPlace();
            doBreak();
        }
        if(MultiThread.getValue()) newThread();
    }

    public void newThread() {
        for (int i = 0; i <= MultiThreadValue.getValue(); i++) {
            if (threadDelay.passedMs(MultiThreadDelay.getValue())) {
                if (thread == null)
                    thread = new Thread((Runnable) AutoCrystal.get(this));
                if (thread != null && (thread.isInterrupted() || thread.isAlive()))
                    thread = new Thread((Runnable) AutoCrystal.get(this));
                if (thread != null && thread.getState().equals(Thread.State.NEW)) {
                    try {
                        thread.start();
                        thread.run();
                    } catch (Exception ignored) {}
                }
                threadDelay.reset();
            }
            threadDelay.reset();
        }
    }
}
