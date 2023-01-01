package cn.origin.cube.module.modules.combat.AutoCrystal;

import cn.origin.cube.Cube;
import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.player.RenderRotationsEvent;
import cn.origin.cube.core.events.player.UpdateWalkingPlayerEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.settings.FloatSetting;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.module.interfaces.Para;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.module.modules.combat.KillAura;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.player.ai.AI;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.client.event.event.ParallelListener;
import cn.origin.cube.utils.client.event.event.Priority;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.InventoryUtil;
import cn.origin.cube.utils.player.RotationUtil;
import cn.origin.cube.utils.player.ai.AIUtils;
import cn.origin.cube.utils.player.ai.CrystalUtils;
import cn.origin.cube.utils.render.Render2DUtil;
import cn.origin.cube.utils.render.Render3DUtil;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Para(para = Para.ParaMode.Full)
@ParallelListener(priority = Priority.HIGHEST)
@ModuleInfo(name = "AutoCrystal", descriptions = "Auto attack entity", category = Category.COMBAT)
public class AutoCrystal extends Module {

    public enum Page{
        PlaceAnBreak, AntiCheat, Render, Other
    }

    public ModeSetting<Page> page = registerSetting("Page", Page.PlaceAnBreak);

    //Place & Break
    public ModeSetting<SmartRange> placeBreakRange = registerSetting("SmartRange", SmartRange.None).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting ak47 = registerSetting("Machine Gun", false).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting place = registerSetting("Place", false).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting explode = registerSetting("Break", false).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting packetPlace = registerSetting("PacketPlace", false).booleanVisible(place).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting packetExplode = registerSetting("PacketExplode", false).booleanVisible(explode).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting smartBreakTrace = registerSetting("smartBreakTrace", false).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting negativeBreakTrace = registerSetting("negativeBreakTrace", false).modeVisible(page, Page.PlaceAnBreak);
    public IntegerSetting placeRange = registerSetting("PlaceRange", 5, 0, 6).modeVisible(page, Page.PlaceAnBreak);
    public IntegerSetting breakRange = registerSetting("BreakRange", 5, 0, 6).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting antiWeakness = registerSetting("AntiWeakness", false).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting silentAntiWeakness = registerSetting("Silent", false).booleanVisible(antiWeakness).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting multiPlace = registerSetting("Multi-Place", false).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting logic = registerSetting("Logic", false).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting placeBreak = registerSetting("PlaceBreak", false).booleanVisible(logic).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting breakPlace = registerSetting("BreakPlace", false).booleanVisible(logic).booleanDisVisible(placeBreak).modeVisible(page, Page.PlaceAnBreak);
    public BooleanSetting swing = registerSetting("Swing", true).modeVisible(page, Page.PlaceAnBreak);
    public ModeSetting<Mode> breakHand = registerSetting("SwingHand", Mode.Main).booleanVisible(swing).modeVisible(page, Page.PlaceAnBreak);
    public IntegerSetting breakSpeed = registerSetting("BreakSpeed", 17 , 0 , 20).modeVisible(page, Page.PlaceAnBreak);
    public IntegerSetting placeSpeed = registerSetting("PlaceSpeed", 18 , 0 , 20).modeVisible(page, Page.PlaceAnBreak);

    //AntiCheat
    public BooleanSetting ncpRange = registerSetting("NcpRange", false).modeVisible(page, Page.AntiCheat);
    public BooleanSetting rotate = registerSetting("Rotate", false).modeVisible(page, Page.AntiCheat);
    public BooleanSetting highPing = registerSetting("High Ping", false).modeVisible(page, Page.AntiCheat);
    public BooleanSetting autoTimerl = registerSetting("Manual-Timer", false).modeVisible(page, Page.AntiCheat);
    public BooleanSetting rayTrace = registerSetting("Ray-trace", false).modeVisible(page, Page.AntiCheat);
    public BooleanSetting predict = registerSetting("Predict", false).modeVisible(page, Page.AntiCheat);
    public BooleanSetting wallCheck = registerSetting("WallCheck", false).modeVisible(page, Page.AntiCheat);
    public BooleanSetting inhibit = registerSetting("Inhibit", false).modeVisible(page, Page.AntiCheat);

    //Render
    public BooleanSetting outline = registerSetting("Outline", true).modeVisible(page, Page.Render);
    public IntegerSetting alpha = registerSetting("Alpha", 150, 0, 255).modeVisible(page, Page.Render);
    public BooleanSetting targetHud = registerSetting("Target Hud", false).modeVisible(page, Page.Render);
    public IntegerSetting tx = registerSetting("Alpha", 150, 0, 1000).modeVisible(page, Page.Render);
    public IntegerSetting ty = registerSetting("Alpha", 150, 0, 1000).modeVisible(page, Page.Render);

    //Other
    public BooleanSetting switchToCrystal = registerSetting("Switch", false).modeVisible(page, Page.Other);
    public BooleanSetting silent = registerSetting("Silent", false).booleanVisible(switchToCrystal).modeVisible(page, Page.Other);
    public BooleanSetting multiThread = registerSetting("MultiThread", false).modeVisible(page, Page.Other);
    public BooleanSetting players = registerSetting("Players", false).modeVisible(page, Page.Other);
    public BooleanSetting mobs = registerSetting("Hostiles", false).modeVisible(page, Page.Other);
    public BooleanSetting passives = registerSetting("Passives", false).modeVisible(page, Page.Other);
    public BooleanSetting antiSuicide = registerSetting("Check", true).modeVisible(page, Page.Other);
    public IntegerSetting minDamage = registerSetting("MinimumDmg", 4, 0, 20).modeVisible(page, Page.Other);
    public IntegerSetting setSelfDamage = registerSetting("SelfDamage", 10, 0, 20).modeVisible(page, Page.Other);
    public BooleanSetting stopWhenEating = registerSetting( "StopWhenEating", false).modeVisible(page, Page.Other);
    public BooleanSetting stopWhenMining = registerSetting("StopWhenMining", false).modeVisible(page, Page.Other);
    public BooleanSetting cancelCrystal = registerSetting("Cancel Crystal", false).modeVisible(page, Page.Other);
    public BooleanSetting check = registerSetting("Check", true).modeVisible(page, Page.Other);
    private final Map<Integer, Long> attackedCrystals = new ConcurrentHashMap<>();
    public final HelperRange rangeHelper = new HelperRange(this);
    private final List<BlockPos> placementPackets = new ArrayList<>();
    private final List<Integer> explosionPackets = new ArrayList<>();
    static AI.HalqPos render = new AI.HalqPos(BlockPos.ORIGIN, 0);
    private final List<Integer> deadCrystals = new ArrayList<>();
    private Timer HoleMiningTimer = new Timer();
    private static boolean togglePitch = false;
    private static boolean cancelingCrystals;
    private static boolean isSpoofingAngles;
    private Timer bypassTimer = new Timer();
    private static boolean autoTimeractive;
    private boolean switchCooldown = false;
    private Timer breakTimer = new Timer();
    private Timer placeTimer = new Timer();
    private boolean isAttacking = false;
    private String arrayListEntityName;
    private static boolean Isthinking;
    private Timer timer = new Timer();
    private static boolean canMine;
    private int x = tx.getValue();
    private int y = ty.getValue();
    private static double pitch;
    public boolean shouldRotate;
    private BlockPos bypassPos;
    boolean smoothRotatePitch;
    boolean smoothRotateYaw;
    boolean smoothRotated;
    int addedOriginYaw;
    int addedInputYaw;
    float smoothPitch;
    private static double yaw;
    private int oldSlot = -1;
    private int height = 100;
    public Entity renderEnt;
    private int width = 200;
    private int newSlot;
    private int breaks;
    private Rotation rotateAngles;

    public static boolean isCancelingCrystals() {
        return cancelingCrystals;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(fullNullCheck())return;
        if(stopWhenEating.getValue() && isEating() || stopWhenMining.getValue() && isMining()) return;
        doLogic();
    }

    @Override
    public void onEnable() {
        render = new AI.HalqPos(BlockPos.ORIGIN, 0);
        super.onEnable();
    }

    public void doLogic(){
        if(logic.getValue() && placeBreak.getValue()){
            PCrystal();
            BCrystal();
        }else if(logic.getValue() && breakPlace.getValue()){
            BCrystal();
            PCrystal();
        }
    }

    public void BCrystal(){
        EntityEnderCrystal crystal = mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityEnderCrystal)
                .map(entity -> (EntityEnderCrystal) entity)
                .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                .orElse(null);
        if (explode.getValue() && crystal != null && mc.player.getDistance(crystal) <= breakRange.getValue() && mc.player.getHealth() >= setSelfDamage.getValue()) {
            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
                if(!silentAntiWeakness.getValue()) {
                    if (!isAttacking) {
                        oldSlot = mc.player.inventory.currentItem;
                        isAttacking = true;
                    }

                    newSlot = -1;
                    for (int i = 0; i < 9; i++) {
                        ItemStack stack = mc.player.inventory.getStackInSlot(i);
                        if (stack == ItemStack.EMPTY) {
                            continue;
                        }
                        if ((stack.getItem() instanceof ItemSword)) {
                            newSlot = i;
                            break;
                        }
                        if ((stack.getItem() instanceof ItemTool)) {
                            newSlot = i;
                            break;
                        }
                    }

                    if (newSlot != -1) {
                        mc.player.inventory.currentItem = newSlot;
                        switchCooldown = true;
                    }
                }else{
                    mc.getConnection().sendPacket(new CPacketHeldItemChange(newSlot));
                }
            }
            if (ak47.getValue()) {
                mc.world.removeEntity(crystal);
            }
            if (highPing.getValue()) {
                crystal.setDead();
                mc.world.removeAllEntities();
                mc.world.getLoadedEntityList();
                placeSpeed.setValue(18);
                breakSpeed.setValue(17);
            }
            rotateTo(crystal.posX, crystal.posY, crystal.posZ, mc.player, false);
            if (predict.getValue()) {//ToDo make better predict

            }
            if (isDesynced()) {
                ReSync();
            }
            if (breakTimer.getPassedTimeMs() / 50 >= 20 - breakSpeed.getValue()) {
                breakTimer.reset();
                breka(crystal);
            }
            breaks++;
            if (breaks == 2 && multiPlace.getValue()) {
                if (rotate.getValue()) {
                    resetRotation();
                }
                breaks = 0;
                return;
            } else if (!multiPlace.getValue() && breaks == 1) {
                if (!multiPlace.getValue()) {
                    resetRotation();
                }
                breaks = 0;
                return;
            }
        } else {
            if (rotate.getValue()) {
                resetRotation();
            }
            if (oldSlot != -1) {
                mc.player.inventory.currentItem = oldSlot;
                oldSlot = -1;
            }
            isAttacking = false;
        }
        if (isSpoofingAngles) {
            if (togglePitch) {
                mc.player.rotationPitch += 0.0004;
                togglePitch = false;
            } else {
                mc.player.rotationPitch -= 0.0004;
                togglePitch = true;
            }
        }

    }


    public BlockPos blockPos;
    public void PCrystal(){
        try {
            int crystalSlot = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? mc.player.inventory.currentItem : -1;
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (mc.player.inventory.getStackInSlot(l).getItem() == Items.END_CRYSTAL) {
                        crystalSlot = l;
                        break;
                    }
                }
            }

            boolean offhand = false;
            if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
                offhand = true;
            } else if (crystalSlot == -1) {
                return;
            }

            List<BlockPos> blocks = findCrystalBlocks();
            List<Entity> entities = new ArrayList<>();
            if (players.getValue()) {
                if (multiThread.getValue()) {
                    Cube.threadManager.run(() -> entities.addAll(mc.world.playerEntities));
                } else {
                    entities.addAll(mc.world.playerEntities);
                }
            }
            entities.addAll(mc.world.loadedEntityList.stream().filter(entity -> EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? passives.getValue() : mobs.getValue())).collect(Collectors.toList()));

            BlockPos q = render.getBlockPos();
            double damage = .5;
            for (Entity entity : entities) {
                if (entity == mc.player || ((EntityLivingBase) entity).getHealth() <= 0) {
                    continue;
                }

                for (BlockPos blockPos : blocks) {
                    double b = entity.getDistanceSq(blockPos);
                    if (b >= 169) {
                        continue;
                    }
                    if(wallCheck.getValue() && !Utils.INSTANCE.canSeePos(blockPos)){
                        continue;
                    }
                    double d = calculateDamage1(blockPos.getX() + .5, blockPos.getY() + 1, blockPos.getZ() + .5, entity);
                    if (d < minDamage.getValue()) {
                        continue;
                    }
                    if (d > damage) {
                        double self = calculateDamage1(blockPos.getX() + .5, blockPos.getY() + 1, blockPos.getZ() + .5, mc.player);
                        if ((self > d && !(d < ((EntityLivingBase) entity).getHealth())) || self - .5 > mc.player.getHealth()) {
                            continue;
                        }
                        damage = d;
                        q = blockPos;
                        renderEnt = entity;
                        arrayListEntityName = renderEnt.getName();
                    }
                }
            }
            if (damage == .5) {
                render = null;
                renderEnt = null;
                if (rotate.getValue()) {
                    resetRotation();
                }
                return;
            }
            render.getBlockPos().equals(q);
            //ToDo Work on Silent switch
            final int oldSlot = KillAura.mc.player.inventory.currentItem;
            if (place.getValue()) {
                if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                    if (switchToCrystal.getValue()) {
                        if (silent.getValue()) {
                            mc.player.connection.sendPacket(new CPacketHeldItemChange(crystalSlot));
                        } else {
                            InventoryUtil.switchToHotbarSlot(crystalSlot, false);
                        }
                        if (rotate.getValue()) {
                            resetRotation();
                        }
                        switchCooldown = true;
                    }
                    return;
                }
                EnumFacing f;
                rotateTo(q.getX() + .5, q.getY() - .5, q.getZ() + .5, mc.player, false);
                if (rayTrace.getValue()) {
                    RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(q.getX() + .5, q.getY() - .5d, q.getZ() + .5));
                    if (result == null || result.sideHit == null) {
                        f = EnumFacing.UP;
                    } else {
                        f = result.sideHit;
                    }
                    if (switchCooldown) {
                        switchCooldown = false;
                        return;
                    }
                } else {
                    f = EnumFacing.UP;
                }
                if (placeTimer.getPassedTimeMs() / 50 >= 20 - placeSpeed.getValue()) {
                    placeTimer.reset();
                    placee(q, f, offhand);
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void ReSync(){
        if (breakTimer.passedTicks(5)) {
            mc.player.setSneaking(true);
            breakTimer.reset();
        }
        mc.player.setSneaking(false);
    }

    @SubscribeEvent
    public void onRenderRotations(RenderRotationsEvent event) {
        if (rotate.getValue()) {
            if (isEnabled() && (renderEnt != null || render != null)) {
                if (rotateAngles != null) {
                    event.setCanceled(true);

                    event.setYaw(rotateAngles.getYaw());
                    event.setPitch(rotateAngles.getPitch());
                }
            }
        }
    }

    public void placee(BlockPos q, EnumFacing f, Boolean offhand) {
        if (packetPlace.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
        } else {
            placeCrystalOnBlock(q, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        }
    }

    public void breka(EntityEnderCrystal crystal){
        if(packetExplode.getValue()){
            mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
            CPacketUseEntity packet = new CPacketUseEntity();
            packet.entityId = crystal.entityId;
            packet.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket(packet);
        }else{
            mc.playerController.attackEntity(mc.player, crystal);
        }
        if(swing.getValue()) {
            mc.player.swingArm(getHandToBreak());
        }else{

        }
        mc.playerController.updateController();
    }

    @SubscribeEvent
    public void onPacketRecieve(PacketEvent.Receive event){
        if(event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            if (this.inhibit.getValue()) {
                try {
                    this.renderEnt = mc.world.getEntityByID(packet.getEntityID());
                } catch (Exception ex) {
                }
            }
        }
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                    if (entity instanceof EntityEnderCrystal) {
                        if (entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) < 36) {
                            entity.setDead();
                            mc.world.removeEntity(entity);
                        }
                    }
                }
            }
        }
    }

    public boolean ableToPlace(BlockPos position) {
        Block placeBlock = mc.world.getBlockState(position).getBlock();

        if (!placeBlock.equals(Blocks.BEDROCK) && !placeBlock.equals(Blocks.OBSIDIAN)) {
            return false;
        }

        BlockPos nativePosition = position.up();
        BlockPos updatedPosition = nativePosition.up();

        Block nativeBlock = mc.world.getBlockState(nativePosition).getBlock();
        if (!nativeBlock.equals(Blocks.AIR) && !nativeBlock.equals(Blocks.FIRE)) {
            return false;
        }

        Block updatedBlock = mc.world.getBlockState(updatedPosition).getBlock();
        if (!updatedBlock.equals(Blocks.AIR) && !updatedBlock.equals(Blocks.FIRE)) {
            return false;
        }

        int unsafeEntities = 0;

        for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
                nativePosition.getX(), position.getY(), nativePosition.getZ(), nativePosition.getX() + 1, nativePosition.getY() + 2.0, nativePosition.getZ() + 1
        ))) {

            if (entity == null || entity.isDead || deadCrystals.contains(entity.getEntityId())) {
                continue;
            }

            if (entity instanceof EntityXPOrb) {
                continue;
            }
            if (entity instanceof EntityEnderCrystal) {

                if (attackedCrystals.containsKey(entity.getEntityId()) && entity.ticksExisted < 20) {
                    continue;
                }

                double localDamage = getDamageFromExplosion(mc.player, entity.getPositionVector(), false);

                double idealDamage = 0;

                for (Entity target : new ArrayList<>(mc.world.loadedEntityList)) {

                    if (target == null || target.equals(mc.player) || target.getEntityId() < 0 || EntityUtil.isDead(target) || Cube.friendManager.isFriend(entity.getName())) {
                        continue;
                    }

                    if (target instanceof EntityEnderCrystal) {
                        continue;
                    }

                    if (target.isBeingRidden() && target.getPassengers().contains(mc.player)) {
                        continue;
                    }

                    if (target instanceof EntityPlayer && !players.getValue() || Utils.INSTANCE.isPassiveMob(target) && !passives.getValue() || Utils.INSTANCE.isNeutralMob(target) && !passives.getValue() || Utils.INSTANCE.isHostileMob(target) && !mobs.getValue()) {
                        continue;
                    }

                    double entityRange = mc.player.getDistance(target);

                    if (entityRange > placeRange.getValue()) {
                        continue;
                    }

                    double targetDamage = getDamageFromExplosion(target, entity.getPositionVector(), false);
                    double safetyIndex = 1;

                    if (Utils.INSTANCE.canTakeDamage()) {

                        double health = mc.player.getHealth();

                        if (localDamage + 0.5 > health) {
                            safetyIndex = -9999;
                        }
                        double efficiency = targetDamage - localDamage;

                        if (efficiency < 0 && Math.abs(efficiency) < 0.25) {
                            efficiency = 0;
                        }

                        safetyIndex = efficiency;

                    }

                    if (safetyIndex < 0) {
                        continue;
                    }

                    if (targetDamage > idealDamage) {
                        idealDamage = targetDamage;
                    }
                }

                if (idealDamage > 2.0) {
                    continue;
                }
            }

            unsafeEntities++;
        }
        return unsafeEntities <= 0;
    }

//    public boolean canFacePlace(EntityLivingBase target) {
//        float healthTarget = target.getHealth() + target.getAbsorptionAmount();
//        if (healthTarget <= BlastHealth.getValue()) {
//            return true;
//        } else if (ArmorCheck.getValue()) {
//            for (ItemStack itemStack : target.getArmorInventoryList()) {
//                if (itemStack.isEmpty()) {
//                    continue;
//                }
//                float dmg = ((float) itemStack.getMaxDamage() - (float) itemStack.getItemDamage()) / (float) itemStack.getMaxDamage();
//                if (dmg <= ArmorRate.getValue() / 100f) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public void placeCrystalOnBlock(BlockPos pos, EnumHand hand) {
        if(multiThread.getValue()){
            Threads threads = new Threads(ThreadType.BLOCK);
            threads.start();
            pos = pos;
        }
        RayTraceResult result = Minecraft.getMinecraft().world
                .rayTraceBlocks(
                        new Vec3d(
                                Minecraft.getMinecraft().player.posX,
                                Minecraft.getMinecraft().player.posY
                                        + (double) Minecraft.getMinecraft().player.getEyeHeight(),
                                Minecraft.getMinecraft().player.posZ),
                        new Vec3d((double) pos.getX() + 0.5, (double) pos.getY() - 0.5, (double) pos.getZ() + 0.5));
        EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
        rotateTo(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, mc.player, false);
        Minecraft.getMinecraft().player.connection
                .sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    @Override
    public void onRender3D(Render3DEvent event){
        if(render != null || renderEnt != null){
            Render3DUtil.drawBlockBox(render.getBlockPos(), new Color(Colors.getGlobalColor().getRed(),Colors.getGlobalColor().getGreen(), Colors.getGlobalColor().getBlue(), 140), outline.getValue(), 3);
        }
    }

    //ToDo add more to this

    @Override
    public void onRender2D() {
        if(targetHud.getValue()) {
            if ((mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)) {
                Render2DUtil.drawBorderedRect(tx.getValue(), ty.getValue(), tx.getValue() + width, ty.getValue() + height, 1, new Color(35, 35, 35, 150).getRGB(), Colors.getGlobalColor().getRGB());
                if(renderEnt != null) {
                    drawHead((mc.getConnection()).getPlayerInfo(renderEnt.getUniqueID()).getLocationSkin(), tx.getValue() + 5, ty.getValue() + 10);
                }
                Cube.fontManager.CustomFont.drawString((renderEnt == null) ? "None" : renderEnt.getName(), tx.getValue() + 43, ty.getValue() + 10, Colors.getGlobalColor().getRGB(), true);
                Cube.fontManager.CustomFont.drawString((renderEnt == null) ? "None" : "" + renderEnt.getDistance(mc.player), tx.getValue() + 43, ty.getValue() + 20, Colors.getGlobalColor().getRGB(), true);
                Render2DUtil.drawGradientHRect(tx.getValue() + 5, ty.getValue() + 55, tx.getValue() + 140, ty.getValue() + 67 - (7), new Color(255, 0, 0).getRGB(), new Color(0, 255, 0).getRGB());
            }
        }
        super.onRender2D();
    }

    public void drawHead(ResourceLocation skin, int width, int height) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(skin);
        Gui.drawScaledCustomSizeModalRect(width, height, 8, 8, 8, 8, 37, 37, 64, 64);
    }

    private List<BlockPos> findCrystalBlocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(Utils.INSTANCE.getPlayerPos(), (float) placeRange.getValue(), (int) placeRange.getValue(), false, true, 0).stream().filter(this::ableToPlace).collect(Collectors.toList()));
        return positions;
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public boolean isEating() {
        ItemStack stack = mc.player.getActiveItemStack();
        return mc.player.isHandActive()
                && !stack.isEmpty()
                && stack.getItem().getItemUseAction(stack) == EnumAction.EAT;
    }

    public boolean isMining() {
        return mc.playerController.getIsHittingBlock();
    }

    public boolean isDesynced() {
        if (mc.isSingleplayer()) {
            return false;
        }
        return explosionPackets.size() > 40 || placementPackets.size() > 40;
    }

//    public float calculateDamage(double posX, double posY, double posZ, Entity entity) {
//        float doubleExplosionSize = 6.0F * 2.0F;
//        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
//        Vec3d vec3d = new Vec3d(posX, posY, posZ);
//        double blockDensity = (double) entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
//        double v = (1.0D - distancedsize) * blockDensity;
//        float damage = (float) ((int) ((v * v + v) / 2.0D * 9.0D * (double) doubleExplosionSize + 1.0D));
//        double finald = 1;
//        if (entity instanceof EntityLivingBase) {
//            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
//        }
//        return (float) finald;
//    }

    public float calculateDamage1(double posX, double posY, double posZ, Entity entity) {
        Vec3d offset = new Vec3d(entity.posX, entity.posY, entity.posZ);
        return calculateDamage(posX, posY, posZ, entity, offset);
    }

    public float calculateDamage(double posX, double posY, double posZ, Entity entity, Vec3d vec) {
        float doubleExplosionSize = 12.0f;
        double distancedsize = getRange(vec, posX, posY, posZ) / doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        } catch (Exception ignore) {
        }
        double v = (1.0 - distancedsize) * blockDensity;
        float damage = (float) ((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            try {
                finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));
            } catch (Exception ignored) {
            }
        }
        return (float) finald;
    }

    public static double getRange(Vec3d a, double x, double y, double z) {
        double xl = a.x - x;
        double yl = a.y - y;
        double zl = a.z - z;
        return Math.sqrt(xl * xl + yl * yl + zl * zl);
    }

    public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage = damage * (1.0F - f / 25.0F);

            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage = damage - (damage / 4);
            }

            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    public float calculateDamage(EntityEnderCrystal crystal, Entity entity) {
        return calculateDamage1(crystal.posX, crystal.posY, crystal.posZ, entity);
    }

    private void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
        cancelingCrystals = true;
        Isthinking = true;
    }

    private void resetRotation() {
        if (isSpoofingAngles) {
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            isSpoofingAngles = false;
        }
    }

    private EnumHand getHandToBreak() {
        if (breakHand.getValue().equals(Mode.Offhand)) {
            return EnumHand.OFF_HAND;
        }else if(breakHand.getValue().equals(Mode.Both)){
            return EnumHand.OFF_HAND;
        }
        return EnumHand.MAIN_HAND;
    }

    public void setBypassPos(BlockPos pos) {
        bypassTimer.reset();
        this.bypassPos = pos;
    }

    private void Thinking() {
        if(Isthinking) {
            this.rayTrace.setValue(true);
            this.placeRange.setValue(6);
            this.breakSpeed.setValue(20);
            this.timer.reset();
        }
    }

    public void CancelingCrystals() {
        if(cancelCrystal.getValue()) {
            mc.world.removeAllEntities();
            mc.world.getLoadedEntityList();
            this.timer.reset();

        }
        if(autoTimerl.getValue()) {
            this.timer.reset();
        }
    }

    public static float getDamageFromExplosion(Entity entity, Vec3d vector, boolean blockDestruction) {
        return calculateExplosionDamage(entity, vector, 6, blockDestruction);
    }

    public static float calculateExplosionDamage(Entity entity, Vec3d vector, float explosionSize, boolean blockDestruction) {

        double doubledExplosionSize = explosionSize * 2.0;
        double dist = entity.getDistance(vector.x, vector.y, vector.z) / doubledExplosionSize;
        if (dist > 1) {
            return 0;
        }

        double v = (1 - dist) * getBlockDensity(blockDestruction, vector, entity.getEntityBoundingBox());
        float damage = CombatRules.getDamageAfterAbsorb(getScaledDamage((float) ((v * v + v) / 2.0 * 7.0 * doubledExplosionSize + 1.0)), ((EntityLivingBase) entity).getTotalArmorValue(), (float) ((EntityLivingBase) entity).getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

        DamageSource damageSource = DamageSource.causeExplosionDamage(new Explosion(entity.world, entity, vector.x, vector.y, vector.z, (float) doubledExplosionSize, false, true));

        int n = EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), damageSource);
        if (n > 0) {
            damage = CombatRules.getDamageAfterMagicAbsorb(damage, n);
        }

        if (((EntityLivingBase) entity).isPotionActive(MobEffects.RESISTANCE)) {
            PotionEffect potionEffect = ((EntityLivingBase) entity).getActivePotionEffect(MobEffects.RESISTANCE);
            if (potionEffect != null) {
                damage = damage * (25.0F - (potionEffect.getAmplifier() + 1) * 5) / 25.0F;
            }
        }

        return Math.max(damage, 0);
    }

    public void LOGIC(){
    }


    public static double getBlockDensity(boolean blockDestruction, Vec3d vector, AxisAlignedBB bb) {

        double diffX = 1 / ((bb.maxX - bb.minX) * 2D + 1D);
        double diffY = 1 / ((bb.maxY - bb.minY) * 2D + 1D);
        double diffZ = 1 / ((bb.maxZ - bb.minZ) * 2D + 1D);
        double diffHorizontal = (1 - Math.floor(1D / diffX) * diffX) / 2D;
        double diffTranslational = (1 - Math.floor(1D / diffZ) * diffZ) / 2D;

        if (diffX >= 0 && diffY >= 0 && diffZ >= 0) {

            float solid = 0;
            float nonSolid = 0;

            for (double x = 0; x <= 1; x = x + diffX) {
                for (double y = 0; y <= 1; y = y + diffY) {
                    for (double z = 0; z <= 1; z = z + diffZ) {

                        double scaledDiffX = bb.minX + (bb.maxX - bb.minX) * x;
                        double scaledDiffY = bb.minY + (bb.maxY - bb.minY) * y;
                        double scaledDiffZ = bb.minZ + (bb.maxZ - bb.minZ) * z;

                        if (!isSolid(new Vec3d(scaledDiffX + diffHorizontal, scaledDiffY, scaledDiffZ + diffTranslational), vector, blockDestruction)) {
                            solid++;
                        }

                        nonSolid++;
                    }
                }
            }

            return solid / nonSolid;
        } else {
            return 0;
        }
    }

    public static boolean isSolid(Vec3d start, Vec3d end, boolean blockDestruction) {

        if (!Double.isNaN(start.x) && !Double.isNaN(start.y) && !Double.isNaN(start.z)) {
            if (!Double.isNaN(end.x) && !Double.isNaN(end.y) && !Double.isNaN(end.z)) {

                int currX = MathHelper.floor(start.x);
                int currY = MathHelper.floor(start.y);
                int currZ = MathHelper.floor(start.z);

                int endX = MathHelper.floor(end.x);
                int endY = MathHelper.floor(end.y);
                int endZ = MathHelper.floor(end.z);

                BlockPos blockPos = new BlockPos(currX, currY, currZ);
                IBlockState blockState = mc.world.getBlockState(blockPos);
                Block block = blockState.getBlock();

                if ((blockState.getCollisionBoundingBox(mc.world, blockPos) != Block.NULL_AABB) && block.canCollideCheck(blockState, false) && !blockDestruction) {
                    RayTraceResult collisionInterCheck = blockState.collisionRayTrace(mc.world, blockPos, start, end);

                    return collisionInterCheck != null;
                }

                double seDeltaX = end.x - start.x;
                double seDeltaY = end.y - start.y;
                double seDeltaZ = end.z - start.z;

                int steps = 200;

                while (steps-- >= 0) {

                    if (Double.isNaN(start.x) || Double.isNaN(start.y) || Double.isNaN(start.z)) {
                        return false;
                    }

                    if (currX == endX && currY == endY && currZ == endZ) {
                        return false;
                    }

                    boolean unboundedX = true;
                    boolean unboundedY = true;
                    boolean unboundedZ = true;

                    double stepX = 999;
                    double stepY = 999;
                    double stepZ = 999;
                    double deltaX = 999;
                    double deltaY = 999;
                    double deltaZ = 999;

                    if (endX > currX) {
                        stepX = currX + 1;
                    } else if (endX < currX) {
                        stepX = currX;
                    } else {
                        unboundedX = false;
                    }

                    if (endY > currY) {
                        stepY = currY + 1.0;
                    } else if (endY < currY) {
                        stepY = currY;
                    } else {
                        unboundedY = false;
                    }

                    if (endZ > currZ) {
                        stepZ = currZ + 1.0;
                    } else if (endZ < currZ) {
                        stepZ = currZ;
                    } else {
                        unboundedZ = false;
                    }

                    if (unboundedX) {
                        deltaX = (stepX - start.x) / seDeltaX;
                    }

                    if (unboundedY) {
                        deltaY = (stepY - start.y) / seDeltaY;
                    }

                    if (unboundedZ) {
                        deltaZ = (stepZ - start.z) / seDeltaZ;
                    }

                    if (deltaX == 0) {
                        deltaX = -1.0E-4;
                    }

                    if (deltaY == 0) {
                        deltaY = -1.0E-4;
                    }

                    if (deltaZ == 0) {
                        deltaZ = -1.0E-4;
                    }

                    EnumFacing facing;

                    if (deltaX < deltaY && deltaX < deltaZ) {
                        facing = endX > currX ? EnumFacing.WEST : EnumFacing.EAST;
                        start = new Vec3d(stepX, start.y + seDeltaY * deltaX, start.z + seDeltaZ * deltaX);
                    } else if (deltaY < deltaZ) {
                        facing = endY > currY ? EnumFacing.DOWN : EnumFacing.UP;
                        start = new Vec3d(start.x + seDeltaX * deltaY, stepY, start.z + seDeltaZ * deltaY);
                    } else {
                        facing = endZ > currZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
                        start = new Vec3d(start.x + seDeltaX * deltaZ, start.y + seDeltaY * deltaZ, stepZ);
                    }

                    currX = MathHelper.floor(start.x) - (facing == EnumFacing.EAST ? 1 : 0);
                    currY = MathHelper.floor(start.y) - (facing == EnumFacing.UP ? 1 : 0);
                    currZ = MathHelper.floor(start.z) - (facing == EnumFacing.SOUTH ? 1 : 0);

                    blockPos = new BlockPos(currX, currY, currZ);
                    blockState = mc.world.getBlockState(blockPos);
                    block = blockState.getBlock();

                    if (block.canCollideCheck(blockState, false) && !blockDestruction) {
                        RayTraceResult collisionInterCheck = blockState.collisionRayTrace(mc.world, blockPos, start, end);

                        return collisionInterCheck != null;
                    }
                }
            }
        }

        return false;
    }

    public final EntityEnderCrystal getBestCrystal() {
        double bestDamage = 0;
        EntityEnderCrystal bestCrystal = null;
        for (Entity e : mc.world.loadedEntityList) {
            if (!(e instanceof EntityEnderCrystal)) continue;
            EntityEnderCrystal crystal = (EntityEnderCrystal) e;
            for (EntityPlayer target : new ArrayList<>(mc.world.playerEntities)) {
                if (mc.player.getDistanceSq(target) > MathUtil.square(placeRange.getValue())) continue;
                if (predict.getValue() && target != mc.player && this.timer.getPassedTimeMs() > this.breakSpeed.getValue().longValue()) {
                    float f = target.width / 2.0F, f1 = target.height;
                    target.setEntityBoundingBox(new AxisAlignedBB(target.posX - (double) f, target.posY, target.posZ - (double) f, target.posX + (double) f, target.posY + (double) f1, target.posZ + (double) f));
                    Entity y = renderEnt;
                    target.setEntityBoundingBox(y.getEntityBoundingBox());
                }
                double targetDamage = this.calculateDamage(crystal, target);
                if (targetDamage == 0) continue;
                if (targetDamage > bestDamage) {
                    bestDamage = targetDamage;
                    bestCrystal = crystal;
                }
            }
        }
        return bestCrystal;
    }

    public static float getScaledDamage(float damage) {
        World world = mc.world;
        if (world == null) {
            return damage;
        }

        switch (mc.world.getDifficulty()) {
            case PEACEFUL:
                return 0;
            case EASY:
                return Math.min(damage / 2.0F + 1.0F, damage);
            case NORMAL:
            default:
                return damage;
            case HARD:
                return damage * 3.0F / 2.0F;
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
                setSmoothRotationYaw = setSmoothRotationYaw(setSmoothRotationYaw, (float) yaw);
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

    public AI.HalqPos placeCalculateAI() {
        AI.HalqPos posToReturn = new AI.HalqPos(BlockPos.ORIGIN, 0.5f);
        for (BlockPos pos : AIUtils.getSphere(placeRange.getValue())) {
            float targetDamage = CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, renderEnt, true);
            float selfDamage = CrystalUtils.calculateDamage(mc.world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, mc.player, true);
            if (CrystalUtils.canPlaceCrystal(pos, check.getValue(), true, multiPlace.getValue(), false)) {
                if (mc.player.getDistance(pos.getX() + 0.5f, pos.getY() + 1.0f, pos.getZ() + 0.5f) > MathUtil.square(placeRange.getValue())) continue;
                if (selfDamage > setSelfDamage.getValue()) continue;
                if (targetDamage < minDamage.getValue()) continue;
                if (antiSuicide.getValue()) if (selfDamage < 2F) continue;
                if (targetDamage > posToReturn.getTargetDamage()) posToReturn = new AI.HalqPos(pos, targetDamage);
            }
        }
        return posToReturn;
    }

    public static AutoCrystal INSTANCE;

    public AutoCrystal() {
        INSTANCE = this;
    }


    public enum Mode{
        Main,
        Offhand,
        Both
    }

    public enum Logic{
        BP,
        PB
    }

    public enum PlaceMode{
        New
    }

    public enum ThreadType{
        BLOCK,
        CRYSTAL
    }

    public enum SmartRange {
        None,
        Normal,
        All,
        Extrapolated
    }

}


final class Threads extends Thread {
    AutoCrystal.ThreadType type;
    BlockPos bestBlock;
    EntityEnderCrystal bestCrystal;

    public Threads(AutoCrystal.ThreadType type) {
        this.type = type;
    }
    @Override
    public void run() {
        if (this.type == AutoCrystal.ThreadType.BLOCK) {
            bestBlock = AutoCrystal.INSTANCE.render.getBlockPos();
            AutoCrystal.INSTANCE.render.getBlockPos().equals(bestBlock);
        } else if (this.type ==AutoCrystal.ThreadType.CRYSTAL) {
            bestCrystal = AutoCrystal.INSTANCE.getBestCrystal();
            AutoCrystal.INSTANCE.renderEnt = bestCrystal;
        }
    }

}
