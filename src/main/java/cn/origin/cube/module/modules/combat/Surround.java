package cn.origin.cube.module.modules.combat;

import cn.origin.cube.Cube;
import cn.origin.cube.event.events.world.Render3DEvent;
import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.interfaces.ModuleInfo;
import cn.origin.cube.core.settings.BooleanSetting;
import cn.origin.cube.core.settings.IntegerSetting;
import cn.origin.cube.utils.Timer;
import cn.origin.cube.utils.client.MathUtil;
import cn.origin.cube.utils.player.BlockUtil;
import cn.origin.cube.utils.player.EntityUtil;
import cn.origin.cube.utils.player.InventoryUtil;
import cn.origin.cube.utils.render.Render3DUtil;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.*;

@ModuleInfo(name = "Surround", descriptions = "Auto place block surround feet", category = Category.COMBAT)
public class Surround extends Module {

    public static boolean isPlacing = false;
    private final IntegerSetting blocksPerTick = registerSetting("BPT", 12, 1, 20);
    private final IntegerSetting delay = registerSetting("Delay", 0, 0, 250);
    private final BooleanSetting noGhost = registerSetting("Packet", false);
    private final BooleanSetting center = registerSetting("TPCenter", false);
    private final BooleanSetting rotate = registerSetting("Rotate", true);
    private final BooleanSetting helpingBlocks = registerSetting("HelpingBlocks", true);
    private final BooleanSetting antiPedo = registerSetting("Always Help", false);
    private final BooleanSetting floor = registerSetting("Floor", false);
    private final BooleanSetting render = registerSetting("Render", false);
    private final BooleanSetting renderString = registerSetting("String Render", false);
    private final IntegerSetting x = registerSetting("X", 40, 0, 1000);
    private final IntegerSetting y = registerSetting("Y", 40, 0, 1000);
    private final Timer timer = new Timer();
    private final Timer retryTimer = new Timer();
    private final Set<Vec3d> extendingBlocks = new HashSet<Vec3d>();
    private final Map<BlockPos, Integer> retries = new HashMap<BlockPos, Integer>();
    private int isSafe;
    private BlockPos startPos;
    private boolean didPlace = false;
    private boolean switchedItem;
    private int lastHotbarSlot;
    private boolean isSneaking;
    private int placements = 0;
    private int extenders = 1;
    private int obbySlot = -1;
    private boolean offHand = false;

    @Override
    public void onEnable() {
        if (fullNullCheck()) {
            disable();
        }
        lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        startPos = getRoundedBlockPos(Surround.mc.player);
        if (center.getValue().booleanValue()) {
        }
        retries.clear();
        retryTimer.reset();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(fullNullCheck())return;
        doFeetPlace();
    }

    @Override
    public void onRender3D(Render3DEvent event){
        if(startPos == null) return;
        if(render.getValue()) {
            Render3DUtil.drawBlockBox(getRoundedBlockPos(mc.player), new Color(255, 60, 58, 100), true, 3);
        }
    }

    @Override
    public void onRender2D(){
        if(render.getValue() && renderString.getValue()){
            Cube.fontManager.CustomFont.drawString((didPlace ? "Placed" : "Not Placed"), x.getValue(), y.getValue(), -1);
        }
    }

    @Override
    public void onDisable() {
        if (fullNullCheck()) {
            return;
        }
        isPlacing = false;
        isSneaking = EntityUtil.stopSneaking(isSneaking);
    }

    private void doFeetPlace() {
        if (check()) {
            return;
        }
        if (mc.player.posY < mc.player.posY) {
            disable();
            return;
        }
        boolean onEChest = mc.world.getBlockState(new BlockPos(mc.player.getPositionVector())).getBlock() == Blocks.ENDER_CHEST;
        if (mc.player.posY - (int)mc.player.posY < 0.7) {
            onEChest = false;
        }
        if (!BlockUtil.isSafe(mc.player, onEChest ? 1:0, floor.getValue())) {
            placeBlocks(mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray(mc.player, onEChest ? 1 : 0, floor.getValue()), helpingBlocks.getValue(), false);
        } else if (!BlockUtil.isSafe(mc.player, onEChest ? 0 : -1, false)) {
            if (antiPedo.getValue()) {
                placeBlocks(mc.player.getPositionVector(), BlockUtil.getUnsafeBlockArray(mc.player, onEChest ? 0 : -1, false), false, false);
            }
        }
        processExtendingBlocks();
        if (didPlace) {
            timer.reset();
        }
    }

    private void processExtendingBlocks() {
        if (extendingBlocks.size() == 2 && extenders < 1) {
            Vec3d[] array = new Vec3d[2];
            int i = 0;
            Iterator<Vec3d> iterator = extendingBlocks.iterator();
            while (iterator.hasNext()) {
                Vec3d vec3d;
                array[i] = vec3d = iterator.next();
                ++i;
            }
            int placementsBefore = placements;
            if (areClose(array) != null) {
                placeBlocks(areClose(array), EntityUtil.getUnsafeBlockArrayFromVec3d(areClose(array), 0, true), true, false);
            }
            if (placementsBefore < placements) {
                extendingBlocks.clear();
            }
        } else if (extendingBlocks.size() > 2 || extenders >= 1) {
            extendingBlocks.clear();
        }
    }

    private Vec3d areClose(Vec3d[] vec3ds) {
        int matches = 0;
        for (Vec3d vec3d : vec3ds) {
            for (Vec3d pos : EntityUtil.getUnsafeBlockArray(Surround.mc.player, 0, true)) {
                if (!vec3d.equals(pos)) continue;
                ++matches;
            }
        }
        if (matches == 2) {
            return Surround.mc.player.getPositionVector().add(vec3ds[0].add(vec3ds[1]));
        }
        return null;
    }


    private boolean placeBlocks(Vec3d pos, Vec3d[] vec3ds, boolean hasHelpingBlocks, boolean isHelping) {
        boolean gotHelp = true;
        block5:
        for (Vec3d vec3d : vec3ds) {
            gotHelp = true;
            BlockPos position = new BlockPos(pos).add(vec3d.x, vec3d.y, vec3d.z);
            switch (BlockUtil.isPositionPlaceable(position, false)) {
                case 1: {
                    if (retries.get(position) == null || retries.get(position) < 4) {
                        placeBlock(position);
                        retries.put(position, retries.get(position) == null ? 1 : retries.get(position) + 1);
                        retryTimer.reset();
                        continue block5;
                    }
                }
                case 2: {
                    if (!hasHelpingBlocks) continue block5;
                    gotHelp = placeBlocks(pos, BlockUtil.getHelpingBlocks(vec3d), false, true);
                }
                case 3: {
                    if (gotHelp) {
                        placeBlock(position);
                    }
                    if (!isHelping) continue block5;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean check() {
        if (fullNullCheck()) {
            return true;
        }
        int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (obbySlot == -1 && eChestSot == -1) {
            toggle();
        }
        offHand = InventoryUtil.isBlock(Surround.mc.player.getHeldItemOffhand().getItem(), BlockObsidian.class);
        isPlacing = false;
        didPlace = false;
        extenders = 1;
        placements = 0;
        obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
        int echestSlot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
        if (isEnabled() == false) {
            return true;
        }
        if (retryTimer.passedMs(2500L)) {
            retries.clear();
            retryTimer.reset();
        }
        if (obbySlot == -1 && !offHand && echestSlot == -1) {
            disable();
            return true;
        }
        isSneaking = EntityUtil.stopSneaking(isSneaking);
        if (Surround.mc.player.inventory.currentItem != lastHotbarSlot && Surround.mc.player.inventory.currentItem != obbySlot && Surround.mc.player.inventory.currentItem != echestSlot) {
            lastHotbarSlot = Surround.mc.player.inventory.currentItem;
        }
        if (!startPos.equals(getRoundedBlockPos(Surround.mc.player))) {
            disable();
            return true;
        }
        return !timer.passedMs(delay.getValue().intValue());
    }

    private void placeBlock(BlockPos pos) {
        if (placements < blocksPerTick.getValue()) {
            int originalSlot = Surround.mc.player.inventory.currentItem;
            int obbySlot = InventoryUtil.findHotbarBlock(BlockObsidian.class);
            int eChestSot = InventoryUtil.findHotbarBlock(BlockEnderChest.class);
            if (obbySlot == -1 && eChestSot == -1) {
                toggle();
            }
            isPlacing = true;
            Surround.mc.player.inventory.currentItem = obbySlot == -1 ? eChestSot : obbySlot;
            Surround.mc.playerController.updateController();
            isSneaking = BlockUtil.placeBlock(pos, offHand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, rotate.getValue(), noGhost.getValue(), isSneaking);
            Surround.mc.player.inventory.currentItem = originalSlot;
            Surround.mc.playerController.updateController();
            didPlace = true;
            ++placements;
        }
    }

    public static BlockPos getRoundedBlockPos(Entity entity) {
        return new BlockPos(roundVec(entity.getPositionVector(), 0));
    }

    public static Vec3d roundVec(Vec3d vec3d, int places) {
        return new Vec3d(MathUtil.round(vec3d.x, places), MathUtil.round(vec3d.y, places), MathUtil.round(vec3d.z, places));
    }


}
