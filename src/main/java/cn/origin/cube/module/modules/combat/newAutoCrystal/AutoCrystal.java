package cn.origin.cube.module.modules.combat.newAutoCrystal;

import cn.origin.cube.Cube;
import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.module.interfaces.Para;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.module.modules.combat.AutoCrystal.Utils;
import cn.origin.cube.module.modules.combat.KillAura;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.player.InventoryUtil;
import cn.origin.cube.core.events.event.event.ParallelListener;
import cn.origin.cube.core.events.event.event.Priority;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Constant(constant = false)
@Para(para = Para.ParaMode.Full)
@ParallelListener(priority = Priority.HIGHEST)
@ModuleInfo(name = "NewAutoCrystal", descriptions = "", category = Category.COMBAT)
public class AutoCrystal extends Module {


    public ModeSetting<Page> page = registerSetting("Page", Page.PlaceBreak);
    //Target
    public IntegerSetting range = registerSetting("Range",  5, 0, 6).modeVisible(page, Page.Target);
    public BooleanSetting players = registerSetting("Players",  false).modeVisible(page, Page.Target);
    public BooleanSetting mobs = registerSetting("Hostiles",  false).modeVisible(page, Page.Target);
    public BooleanSetting passives = registerSetting("Passives",  false).modeVisible(page, Page.Target);
    public IntegerSetting minDamage = registerSetting("MinimumDmg",  4, 0, 20).modeVisible(page, Page.Target);
    public IntegerSetting selfDamage = registerSetting("SelfDamage",  10, 0, 20).modeVisible(page, Page.Target);

    //PlaceAndBreak
    public ModeSetting<logic> loogic = registerSetting("Logic", logic.BreakPlace).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting place = registerSetting("Place",  false).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting explode = registerSetting("Break",  false).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting packetPlace = registerSetting("PacketPlace",  false).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting packetExplode = registerSetting("PacketBreak",  false).modeVisible(page, Page.PlaceBreak);
    public IntegerSetting breakSpeed = registerSetting("BreakSpeed",  20, 0, 20).booleanVisible(explode).modeVisible(page, Page.PlaceBreak);
    public IntegerSetting placeSpeed = registerSetting("PlaceSpeed",  20, 0, 20).booleanVisible(place).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting antiWeakness = registerSetting("AntiWeakness",  false).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting multiPlace = registerSetting("Multi-Place",  false).modeVisible(page, Page.PlaceBreak);
    public IntegerSetting setSelfDamage = registerSetting("SelfDamage", 10, 0, 20).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting wallCheck = registerSetting("WallCheck", false).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting sync = registerSetting("Sync", false).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting predict = registerSetting("Predict", false).modeVisible(page, Page.PlaceBreak);
    public BooleanSetting inhibit = registerSetting("Inhibit", false).modeVisible(page, Page.PlaceBreak);

    //Other
    public BooleanSetting switchToCrystal = registerSetting("Switch", false).modeVisible(page, Page.Other);
    public BooleanSetting rotate = registerSetting("Rotate",  false).modeVisible(page, Page.Other);
    public BooleanSetting autoTimer = registerSetting("Manual-Timer",  false).modeVisible(page, Page.Other);
    public BooleanSetting rayTrace = registerSetting("Ray-trace",  false).modeVisible(page, Page.Other);
    public ModeSetting<bHand> breakHand = registerSetting("Swing",  bHand.Mainhand).modeVisible(page, Page.Other);
    public BooleanSetting thinking = registerSetting("Thinking",  false).modeVisible(page, Page.Other);
    public BooleanSetting cancelCrystal = registerSetting("Cancel Crystal", true).modeVisible(page, Page.Other);

    //Render
    public BooleanSetting renderPos = registerSetting("Render", true).modeVisible(page, Page.Render);
    public BooleanSetting fade = registerSetting("Fade", false).modeVisible(page, Page.Render);
    public BooleanSetting slide = registerSetting("Slide", false).modeVisible(page, Page.Render);

    private BlockPos render;

    private Entity renderEnt;
    private long systemTime = -1;
    private static boolean togglePitch = false;
    private boolean switchCooldown = false;
    private boolean isAttacking = false;
    private int oldSlot = -1;
    private int newSlot;
    private int breaks;
    private String arrayListEntityName;
    private static boolean autoTimeractive;
    private static boolean cancelingCrystals;
    private static boolean Isthinking;
    private static boolean isSpoofingAngles;
    private final ArrayList<Integer> blacklist = new ArrayList();
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
    private final List<Integer> deadCrystals = new ArrayList<>();
    private final Map<Integer, Long> attackedCrystals = new ConcurrentHashMap<>();
    private Timer breakTimer = new Timer();
    private Timer placeTimer = new Timer();

    public static boolean isCancelingCrystals() {
        return cancelingCrystals;
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        doLogic();
    }

    public void doLogic(){
        if(loogic.getValue().equals(logic.PlaceBreak)){
            PCrystal();
            BCrystal();
            PCrystal();
        }
        if(loogic.getValue().equals(logic.BreakPlace)){
            BCrystal();
            PCrystal();
            BCrystal();
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
                entities.addAll(mc.world.playerEntities);
            }
            entities.addAll(mc.world.loadedEntityList.stream().filter(entity -> EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? passives.getValue() : mobs.getValue())).collect(Collectors.toList()));

            BlockPos q = render;
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
                    double d = calculateDamage(blockPos.getX() + .5, blockPos.getY() + 1, blockPos.getZ() + .5, entity);
                    if (d < minDamage.getValue()) {
                        continue;
                    }
                    if (d > damage) {
                        double self = calculateDamage(blockPos.getX() + .5, blockPos.getY() + 1, blockPos.getZ() + .5, mc.player);
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
            render = q;
            //ToDo Work on Silent switch
            final int oldSlot = KillAura.mc.player.inventory.currentItem;
            if (place.getValue()) {
                if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                    if (switchToCrystal.getValue()) {
                        InventoryUtil.switchToHotbarSlot(crystalSlot, false);
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

    public void BCrystal(){
        EntityEnderCrystal crystal = mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityEnderCrystal)
                .map(entity -> (EntityEnderCrystal) entity)
                .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                .orElse(null);
        if (explode.getValue() && crystal != null && mc.player.getDistance(crystal) <= range.getValue() && mc.player.getHealth() >= setSelfDamage.getValue()) {
            if (antiWeakness.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
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
            }
            for (final Entity entity : new ArrayList<Entity>(mc.world.loadedEntityList)) {
                if (this.blacklist.contains(crystal.entityId) && this.inhibit.getValue()) {
                    continue;
                }
            }
            this.blacklist.add(crystal.entityId);
            rotateTo(crystal.posX, crystal.posY, crystal.posZ, mc.player, false);
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

    public void placee(BlockPos q, EnumFacing f, Boolean offhand) {
        if (packetPlace.getValue()) {
            rotateTo(q.getX() + 0.5, q.getY() - 0.5, q.getZ() + 0.5, mc.player, false);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
        } else {
            placeCrystalOnBlock(q, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        }
    }

    public void placeCrystalOnBlock(BlockPos pos, EnumHand hand) {
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
        mc.player.swingArm(getHandToBreak());
        mc.playerController.updateController();
    }

    private void lookAtPacket(double px, double py, double pz, EntityPlayer me) {
        double[] v = EntityUtil.calculateLookAt(px, py, pz, me);
        setYawAndPitch((float) v[0], (float) v[1]);
    }

    private boolean canPlaceCrystal(BlockPos blockPos) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boost2 = blockPos.add(0, 2, 0);
        if ((mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK
                && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
                || mc.world.getBlockState(boost).getBlock() != Blocks.AIR
                || mc.world.getBlockState(boost2).getBlock() != Blocks.AIR
                || !mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty()) {
            return false;
        }
        return true;
    }

    public BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
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

    public float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 6.0F * 2.0F;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = (double) entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedsize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 9.0D * (double) doubleExplosionSize + 1.0D));
        double finald = 1;
        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finald;
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
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, entity);
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
        if (breakHand.getValue().equals(bHand.Offhand)) {
            return EnumHand.OFF_HAND;
        }
        return EnumHand.MAIN_HAND;
    }

    private void Thinking() {
        if(Isthinking) {
            this.rayTrace.setValue(true);
            this.range.setValue(6);
            this.breakSpeed.setValue(20);
            this.breakTimer.reset();
        }
    }

    public void CancelingCrystals() {
        if (cancelCrystal.getValue()) {
            this.thinking.setValue(true);
            mc.world.removeAllEntities();
            mc.world.getLoadedEntityList();
            this.breakTimer.reset();

        }
    }

    public void autoTimer() {
        if(autoTimer.getValue()) {
            this.breakTimer.reset();
        }
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

                    if (entityRange > range.getValue()) {
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

    private List<BlockPos> findCrystalBlocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(Utils.INSTANCE.getPlayerPos(), (float) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(this::ableToPlace).collect(Collectors.toList()));
        return positions;
    }

    @Override
    public void onRender3D(Render3DEvent event){
        if(render == null) return;
        if(render != null && renderPos.getValue()){
            Render3DUtil.drawBlockBox(render, new Color(Colors.getGlobalColor().getRed(), Colors.getGlobalColor().getGreen(),Colors.getGlobalColor().getBlue(), 130), true, 2F);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect && this.sync.getValue()) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity entity : new ArrayList<Entity>(mc.world.loadedEntityList)) {
                    if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) <= 36.0) {
                        entity.setDead();
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public void onPredict(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject && this.predict.getValue()) {
            SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            if (packet.getType() != 51) {
                return;
            }
            if (this.renderEnt == null) {
                return;
            }
            EntityEnderCrystal crystal = new EntityEnderCrystal((World)mc.world, packet.getX(), packet.getY(), packet.getZ());
            if (this.blacklist.contains(packet.getEntityID()) && this.inhibit.getValue()) {
                return;
            }
            CPacketUseEntity crystalPacket = new CPacketUseEntity();
            crystalPacket.entityId = packet.getEntityID();
            crystalPacket.action = CPacketUseEntity.Action.ATTACK;
            mc.player.connection.sendPacket((Packet)crystalPacket);
            if (mc.playerController.getCurrentGameType() != GameType.SPECTATOR) {
                mc.player.resetCooldown();
            }
            mc.player.swingArm(EnumHand.MAIN_HAND);
            this.blacklist.add(packet.getEntityID());
        }
    }

    public enum logic{
        PlaceBreak,BreakPlace
    }

    public enum bHand{
        Offhand,Mainhand
    }

    public enum Page{
        PlaceBreak,Other,Target,Render
    }
}
