package cn.origin.cube.module.modules.combat;

import cn.origin.cube.Cube;
import cn.origin.cube.event.events.world.Render3DEvent;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;
import cn.origin.cube.module.modules.client.AutoConfig;
import cn.origin.cube.module.modules.client.ClickGui;
import cn.origin.cube.settings.BooleanSetting;
import cn.origin.cube.settings.IntegerSetting;
import cn.origin.cube.settings.ModeSetting;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//ToDo add mutlithread cause like idk just seems usefull or smth
@ModuleInfo(name = "AutoCrystal", descriptions = "Auto attack entity", category = Category.COMBAT)
public class AutoCrystal extends Module {

    public BooleanSetting switchToCrystal = registerSetting("Switch", false);
    public BooleanSetting multiThread = registerSetting("MultiThread", false);
    public BooleanSetting players = registerSetting("Players", false);
    public BooleanSetting mobs = registerSetting("Hostiles", false);
    public BooleanSetting passives = registerSetting("Passives", false);
    public BooleanSetting place = registerSetting("Place", false);
    public BooleanSetting explode = registerSetting("Break", false);
    public IntegerSetting range = registerSetting("Range", 5, 0, 6);
    public IntegerSetting minDamage = registerSetting("MinimumDmg", 4, 0, 20);
    public IntegerSetting selfDamage = registerSetting("SelfDamage", 10, 0, 20);
    public BooleanSetting antiWeakness = registerSetting("AntiWeakness", false);
    public BooleanSetting multiPlace = registerSetting("Multi-Place", false);
    public BooleanSetting rotate = registerSetting("Rotate", false);
    public BooleanSetting autoTimerl = registerSetting("Manual-Timer", false);
    public BooleanSetting rayTrace = registerSetting("Ray-trace", false);
    public BooleanSetting predict = registerSetting("Predict", false);
    public BooleanSetting packetPlace = registerSetting("PacketPlace", false);
    public ModeSetting<Mode> breakHand = registerSetting("Swing", Mode.Main);
    public IntegerSetting breakSpeed = registerSetting("BreakSpeed", 20, 0, 20);
    public IntegerSetting placeSpeed = registerSetting("PlaceSpeed", 20, 0, 20);
    public BooleanSetting thinking = registerSetting("Thinking", false);
    public BooleanSetting cancelCrystal = registerSetting("Cancel Crystal", true);
    public BooleanSetting outline = registerSetting("Outline", true);
    public IntegerSetting alpha = registerSetting("Alpha", 150, 0, 255);


    private BlockPos render;
    private Entity renderEnt;
    private static boolean togglePitch = false;
    private boolean switchCooldown = false;
    private boolean isAttacking = false;
    private int oldSlot = -1;
    private int newSlot;
    private int breaks;
    private String arrayListEntityName;

    private Timer timer = new Timer();

    public static boolean isCancelingCrystals() {
        return cancelingCrystals;
    }

    @Override
    public void onUpdate() {
        EntityEnderCrystal crystal = mc.world.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityEnderCrystal)
                .map(entity -> (EntityEnderCrystal) entity)
                .min(Comparator.comparing(c -> mc.player.getDistance(c)))
                .orElse(null);
        if (explode.getValue() && crystal != null && mc.player.getDistance(crystal) <= range.getValue() && mc.player.getHealth() >= selfDamage.getValue()) {
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
            lookAtPacket(crystal.posX, crystal.posY, crystal.posZ, mc.player);
            if (predict.getValue()) {
                final CPacketUseEntity attackPacket = new CPacketUseEntity();
                mc.player.connection.sendPacket((Packet)attackPacket);
            }
            if (timer.getPassedTimeMs() / 50 >= 20 - breakSpeed.getValue()) {
                timer.reset();
                mc.player.swingArm(getHandToBreak());
                mc.playerController.attackEntity(mc.player, crystal);
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
            if(multiThread.getValue()) {
                Cube.threadManager.run(() -> entities.addAll(mc.world.playerEntities));
            }else {
                entities.addAll(mc.world.playerEntities);
            }
        }
        entities.addAll(mc.world.loadedEntityList.stream().filter(entity -> EntityUtil.isLiving(entity) && (EntityUtil.isPassive(entity) ? passives.getValue() : mobs.getValue())).collect(Collectors.toList()));

        BlockPos q = null;
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

        if (place.getValue()) {
            if (!offhand && mc.player.inventory.currentItem != crystalSlot) {
                if (switchToCrystal.getValue()) {
                    mc.player.inventory.currentItem = crystalSlot;
                    if (rotate.getValue()) {
                        resetRotation();
                    }
                    switchCooldown = true;
                }
                return;
            }
            EnumFacing f;
            lookAtPacket(q.getX() + .5, q.getY() - .5, q.getZ() + .5, mc.player);
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
            if (timer.getPassedTimeMs() / 50 >= 20 - placeSpeed.getValue()) {
                timer.reset();
                if(packetPlace.getValue()) {
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(q, f, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0, 0, 0));
                }else {
                    placeCrystalOnBlock(q, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
                }
            }
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
        lookAtPacket(pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, mc.player);
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
            Render3DUtil.drawBlockBox(render, new Color(ClickGui.getCurrentColor().getRed(),ClickGui.getCurrentColor().getGreen(),ClickGui.getCurrentColor().getBlue(), alpha.getValue()), outline.getValue(), 3);
        }
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

    private List<BlockPos> findCrystalBlocks() {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(getPlayerPos(), (float) range.getValue(), (int) range.getValue(), false, true, 0).stream().filter(this::canPlaceCrystal).collect(Collectors.toList()));
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

    private static boolean autoTimeractive;
    private static boolean cancelingCrystals;
    private static boolean Isthinking;
    private static boolean isSpoofingAngles;
    private static double yaw;
    private static double pitch;

    private void setYawAndPitch(float yaw1, float pitch1) {
        yaw = yaw1;
        pitch = pitch1;
        isSpoofingAngles = true;
        cancelingCrystals = true;
        Isthinking = true;
    }
    //unlocked spoofing of angles
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
        }
        return EnumHand.MAIN_HAND;
    }

    private void Thinking() {
        if(Isthinking) {
            this.rayTrace.setValue(true);
            this.range.setValue(6);
            this.breakSpeed.setValue(20);
            this.timer.reset();
        }
    }

    public void CancelingCrystals() {
        if(cancelCrystal.getValue()) {
            this.thinking.setValue(true);
            mc.world.removeAllEntities();
            mc.world.getLoadedEntityList();
            this.timer.reset();

        }
            if(autoTimerl.getValue()) {

                this.timer.reset();
            }
    }

    public static AutoCrystal INSTANCE;

    public AutoCrystal() {
        INSTANCE = this;
    }


    public enum Mode{
        Main,Offhand
    }
}
