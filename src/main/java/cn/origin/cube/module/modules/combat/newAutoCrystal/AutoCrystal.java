package cn.origin.cube.module.modules.combat.newAutoCrystal;

import cn.origin.cube.Cube;
import cn.origin.cube.core.events.client.PacketEvent;
import cn.origin.cube.core.events.world.Render3DEvent;
import cn.origin.cube.core.module.Category;
import cn.origin.cube.core.module.Module;
import cn.origin.cube.core.module.interfaces.Constant;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.DoubleSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.core.settings.ModeSetting;
import cn.origin.cube.core.module.interfaces.ModuleInfo;
import cn.origin.cube.core.module.interfaces.Para;
import cn.origin.cube.module.modules.client.Colors;
import cn.origin.cube.module.modules.combat.AutoCrystal.Utils;
import cn.origin.cube.module.modules.combat.KillAura;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.InventoryUtil;
import cn.origin.cube.core.events.event.event.ParallelListener;
import cn.origin.cube.core.events.event.event.Priority;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.ai.CrystalUtils;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
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
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
import net.minecraft.util.NonNullList;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Constant(constant = false)
@Para(para = Para.ParaMode.Full)
@ParallelListener(priority = Priority.HIGHEST)
@ModuleInfo(name = "NewAutoCrystal", descriptions = "", category = Category.COMBAT)
public class AutoCrystal extends Module {

    BooleanSetting place = registerSetting("Place", true);
    DoubleSetting placeDelay = registerSetting("PlaceDelay", 0.0,0.0,400.0);
    DoubleSetting placeRange = registerSetting("PlaceDelay", 5.0,0.0,6.0);
    BooleanSetting explode = registerSetting("Break", true);
    BooleanSetting packetBreak = registerSetting("Break", true);
    BooleanSetting predictsBreak = registerSetting("Break", true);
    BooleanSetting fastUpdate = registerSetting("Break", true);
    DoubleSetting breakDelay = registerSetting("PlaceDelay", 0.0,0.0,400.0);
    DoubleSetting breakRange = registerSetting("PlaceDelay", 5.0,0.0,6.0);
    BooleanSetting rotate = registerSetting("Break", true);
    BooleanSetting antiSuicide = registerSetting("Break", true);
    DoubleSetting raytrace = registerSetting("PlaceDelay", 5.0,0.0,6.0);
    DoubleSetting targetRange = registerSetting("PlaceDelay", 5.0,0.0,6.0);
    DoubleSetting minPlaceDmg = registerSetting("PlaceDelay", 5.0,0.0,6.0);
    DoubleSetting minBreakDmg = registerSetting("PlaceDelay", 5.0,0.0,6.0);
    DoubleSetting maxSelfDmg = registerSetting("PlaceDelay", 5.0,0.0,6.0);
    ModeSetting<SwingMode> breakSwing = registerSetting("BreakSwing", SwingMode.MainHand);
    private final Timer placeTimer;
    private final Timer breakTimer;
    private EntityEnderCrystal crystal;
    private boolean rotating;
    private String displaytarget;
    private EntityPlayer target;
    private float pitch;
    private float yaw;
    private BlockPos pos;

    public AutoCrystal() {
        this.placeTimer = new Timer();
        this.breakTimer = new Timer();

        this.rotating = false;
        this.pitch = 0.0f;
        this.yaw = 0.0f;
    }

    @Override
    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.target = null;
        this.displaytarget = null;
        this.crystal = null;
        this.pos = null;
    }

    @Override
    public void onDisable() {
        this.rotating = false;
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (mc.currentScreen instanceof GuiChest && mc.world.getBlockState(mc.player.rayTrace(4.5, mc.getRenderPartialTicks()).getBlockPos()).getBlock() == Blocks.ENDER_CHEST) {
            mc.displayGuiScreen((GuiScreen)null);
        }
        if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL) {
            this.target = null;
            this.pos = null;
            return;
        }
        if (this.target == null) {
            this.target = this.getTarget();
        }
        if (this.target == null) {
            this.crystal = null;
            return;
        }
        this.crystal = (EntityEnderCrystal)mc.world.loadedEntityList.stream().filter(this::$Hit1).map(crystal -> crystal).min(Comparator.comparing(crystal -> this.target.getDistance(crystal))).orElse(null);
        if (this.crystal != null && this.explode.getValue() && this.breakTimer.passedMs(this.breakDelay.getValue().longValue())) {
            this.breakTimer.reset();
            if (this.packetBreak.getValue()) {
                this.rotateTo((Entity)this.crystal);
                attackEntity((Entity)this.crystal, true, (this.breakSwing.getValue() == SwingMode.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
            }
            else {
                this.rotateTo((Entity)this.crystal);
                attackEntity((Entity)this.crystal, false, (this.breakSwing.getValue() == SwingMode.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
                mc.player.resetCooldown();
            }
        }
        if (this.place.getValue() && this.placeTimer.passedMs(this.placeDelay.getValue().longValue())) {
            this.placeTimer.reset();
            double damage = 1.5;
            for (final BlockPos blockPos : this.possiblePlacePositions(this.placeRange.getValue().floatValue())) {
                if (this.target != null && blockPos != null && !this.target.isDead) {
                    if (this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
                        continue;
                    }
                    final double targetDamage = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)this.target);
                    if (targetDamage < this.minPlaceDmg.getValue()) {
                        continue;
                    }
                    if (damage >= targetDamage) {
                        continue;
                    }
                    this.pos = blockPos;
                    damage = targetDamage;
                }
            }
            if (damage == 1.5) {
                this.target = null;
                this.pos = null;
                return;
            }
            if (this.pos != null) {
                this.rotateToPos(this.pos);
                placeCrystalOnBlock(this.pos, (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            }
        }
    }

    private void rotateTo(final Entity entity) {
        if (this.rotate.getValue()) {
            final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    private void rotateToPos(final BlockPos pos) {
        if (this.rotate.getValue()) {
            final float[] angle = calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)(pos.getX() + 0.5f), (double)(pos.getY() - 0.5f), (double)(pos.getZ() + 0.5f)));
            this.yaw = angle[0];
            this.pitch = angle[1];
            this.rotating = true;
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.rotate.getValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = event.getPacket();
            packet.yaw = this.yaw;
            packet.pitch = this.pitch;
            this.rotating = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        try {
            if (this.predictsBreak.getValue() && event.getPacket() instanceof SPacketSpawnObject) {
                final SPacketSpawnObject packet = event.getPacket();
                if (packet.getType() == 51 && this.$Hit2(new BlockPos(packet.getX(), packet.getY(), packet.getZ()))) {
                    final CPacketUseEntity predict = new CPacketUseEntity();
                    predict.entityId = packet.getEntityID();
                    predict.action = CPacketUseEntity.Action.ATTACK;
                    mc.player.connection.sendPacket((Packet)predict);
                }
            }
            if (event.getPacket() instanceof SPacketSoundEffect) {
                final SPacketSoundEffect packet2 = event.getPacket();
                if (this.fastUpdate.getValue() && this.crystal != null && packet2.getCategory() == SoundCategory.BLOCKS && packet2.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE && this.$Hit2(new BlockPos(packet2.getX(), packet2.getY(), packet2.getZ()))) {
                    this.crystal.setDead();
                }
            }
        }
        catch (NullPointerException ex) {}
    }

    @Override
    public void onRender3D(final Render3DEvent event) {
        if (this.pos != null && this.target != null) {
            
        }
    }

    private EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        for (final EntityPlayer entity : mc.world.playerEntities) {
            if (mc.player != null && !mc.player.isDead && !entity.isDead && entity.getHealth() + entity.getAbsorptionAmount() > 0.0f && !entity.getName().equals(mc.player.getName()) && !entity.isCreative() && !isInHole((Entity)entity)) {
                if (entity.getDistance((Entity)mc.player) > this.targetRange.getValue()) {
                    continue;
                }
                if (closestPlayer == null) {
                    closestPlayer = entity;
                }
                else {
                    if (closestPlayer.getDistance((Entity)mc.player) <= entity.getDistance((Entity)mc.player)) {
                        continue;
                    }
                    closestPlayer = entity;
                }
            }
        }
        return closestPlayer;
    }

    private boolean $Hit1(final Entity p_Entity) {
        if (p_Entity == null) {
            return false;
        }
        if (!(p_Entity instanceof EntityEnderCrystal)) {
            return false;
        }
        if (p_Entity.isDead) {
            return false;
        }
        if (this.target == null) {
            return false;
        }
        if (Math.sqrt(mc.player.getDistanceSq(p_Entity.posX, p_Entity.posY, p_Entity.posZ)) > this.breakRange.getValue()) {
            return false;
        }
        if (!mc.player.canEntityBeSeen(p_Entity) && Math.sqrt(mc.player.getDistanceSq(p_Entity.posX, p_Entity.posY, p_Entity.posZ)) > this.raytrace.getValue()) {
            return false;
        }
        if (this.target.isDead || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
            return false;
        }
        final double selfDamage = calculateDamage(p_Entity.posX, p_Entity.posY, p_Entity.posZ, (Entity)mc.player);
        final double targetDamage = calculateDamage(p_Entity.posX, p_Entity.posY, p_Entity.posZ, (Entity)this.target);
        return selfDamage + (this.antiSuicide.getValue() ? 2.0 : 0.5) < mc.player.getHealth() + mc.player.getAbsorptionAmount() && selfDamage - 0.5 <= this.maxSelfDmg.getValue() && (targetDamage >= this.target.getHealth() + this.target.getAbsorptionAmount() || targetDamage >= this.minBreakDmg.getValue());
    }

    private boolean $Hit2(final BlockPos packet) {
        if (packet == null) {
            return false;
        }
        if (this.target == null) {
            return false;
        }
        if (Math.sqrt(mc.player.getDistanceSq((double)packet.getX(), (double)packet.getY(), (double)packet.getZ())) > this.breakRange.getValue()) {
            return false;
        }
        if (!canBlockBeSeen(packet.getX(), packet.getY(), packet.getZ()) && Math.sqrt(mc.player.getDistanceSq((double)packet.getX(), (double)packet.getY(), (double)packet.getZ())) > this.raytrace.getValue()) {
            return false;
        }
        if (this.target.isDead || this.target.getHealth() + this.target.getAbsorptionAmount() <= 0.0f) {
            return false;
        }
        final double selfDamage = calculateDamage(packet.getX(), packet.getY(), packet.getZ(), (Entity)mc.player);
        final double targetDamage = calculateDamage(packet.getX(), packet.getY(), packet.getZ(), (Entity)this.target);
        return selfDamage + (this.antiSuicide.getValue() ? 2.0 : 0.5) < mc.player.getHealth() + mc.player.getAbsorptionAmount() && selfDamage - 0.5 <= this.maxSelfDmg.getValue() && (targetDamage >= this.target.getHealth() + this.target.getAbsorptionAmount() || targetDamage >= this.minBreakDmg.getValue());
    }

    private List<BlockPos> possiblePlacePositions(final float placeRange) {
        final NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll((Collection)getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> CrystalUtils.canPlaceCrystal(pos)).collect((Collectors.toList())));
        final double[] selfDamage = new double[1];
        positions.removeIf(blockPos -> {
            if (!canBlockBeSeen(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5) && Math.sqrt(mc.player.getDistanceSq(blockPos.getX() + 0.5, (double)(blockPos.getY() + 1), blockPos.getZ() + 0.5)) > this.raytrace.getValue()) {
                return true;
            }
            else {
                selfDamage[0] = calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, (Entity)mc.player);
                return selfDamage[0] + (this.antiSuicide.getValue() ? 2.0 : 0.5) >= mc.player.getHealth() + mc.player.getAbsorptionAmount() || selfDamage[0] - 0.5 > this.maxSelfDmg.getValue();
            }
        });
        return (List<BlockPos>)positions;
    }

    public static float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        float finalDamage = 1.0f;
        try {
            final float doubleExplosionSize = 12.0f;
            final double distancedSize = entity.getDistance(posX, posY, posZ) / doubleExplosionSize;
            final double blockDensity = entity.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
            final double v = (1.0 - distancedSize) * blockDensity;
            final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * doubleExplosionSize + 1.0);
            if (entity instanceof EntityLivingBase) {
                finalDamage = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(damage), new Explosion((World)mc.world, (Entity)null, posX, posY, posZ, 6.0f, false, true));
            }
        }
        catch (NullPointerException ex) {}
        return finalDamage;
    }

    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)ep.getTotalArmorValue(), (float)ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            final int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            final float f = MathHelper.clamp((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float)entity.getTotalArmorValue(), (float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getDamageMultiplied(final float damage) {
        final int diff = mc.world.getDifficulty().getId();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }

    public static boolean canBlockBeSeen(final double x, final double y, final double z) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(x, y + 1.7, z), false, true, false) == null;
    }

    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final List<BlockPos> circleBlocks = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                for (int y = sphere ? (cy - (int)r) : cy; y < (sphere ? (cy + r) : ((float)(cy + h))); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }

    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand) {
        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, hand, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ()));
    }

    public static float[] calcAngle(final Vec3d from, final Vec3d to) {
        final double difX = to.x - from.x;
        final double difY = (to.y - from.y) * -1.0;
        final double difZ = to.z - from.z;
        final double dist = MathHelper.sqrt(difX * difX + difZ * difZ);
        return new float[] { (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difZ, difX)) - 90.0), (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(difY, dist))) };
    }

    public static void attackEntity(final Entity entity, final boolean packet, final EnumHand hand) {
        if (packet) {
            mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity));
        }
        else {
            mc.playerController.attackEntity((EntityPlayer)mc.player, entity);
        }
        mc.player.swingArm(hand);
    }

    public static boolean isInHole(final Entity entity) {
        return isBedrockHole(new BlockPos(entity.posX, entity.posY, entity.posZ)) || isObbyHole(new BlockPos(entity.posX, entity.posY, entity.posZ)) || isBothHole(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static boolean isObbyHole(final BlockPos blockPos) {
        for (final BlockPos pos : new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }) {
            final IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBedrockHole(final BlockPos blockPos) {
        for (final BlockPos pos : new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }) {
            final IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || touchingState.getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBothHole(final BlockPos blockPos) {
        for (final BlockPos pos : new BlockPos[] { blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.down() }) {
            final IBlockState touchingState = mc.world.getBlockState(pos);
            if (touchingState.getBlock() == Blocks.AIR || (touchingState.getBlock() != Blocks.BEDROCK && touchingState.getBlock() != Blocks.OBSIDIAN)) {
                return false;
            }
        }
        return true;
    }
    
    public enum SwingMode
    {
        MainHand,
        OffHand;
    }

    public enum Settings
    {
        PLACE,
        BREAK,
        MISC,
        RENDER;
    }
}
