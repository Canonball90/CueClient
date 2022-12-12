package dev.canon.cue.module.modules.visual;

import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.module.modules.client.ClickGui;
import dev.canon.cue.settings.BooleanSetting;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;

@ModuleInfo(name = "XRay", descriptions = "Always light", category = Category.VISUAL)
public class XRay extends Module {
    public static ArrayList<Block> xrayBlocks;
    public static XRay INSTANCE;

    BooleanSetting water = registerSetting("Water", true);
    BooleanSetting lava = registerSetting("Lava", true);
    BooleanSetting diamond = registerSetting("Diamond", true);
    BooleanSetting iron = registerSetting("Iron", true);
    BooleanSetting gold = registerSetting("Gold", true);
    BooleanSetting coal = registerSetting("Coal", true);
    BooleanSetting stone = registerSetting("Stone", true);
    BooleanSetting quartz = registerSetting("Quartz", true);
    BooleanSetting lapiz = registerSetting("Lapiz", true);
    BooleanSetting obsidian = registerSetting("Obsidian", true);
    BooleanSetting redstone = registerSetting("Redstone", true);

    public XRay() {
        INSTANCE = this;
        initXray();
    }

    @Override
    public void onEnable() {
        if (mc.world == null) return;
        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onDisable() {
        if (mc.world == null) return;
        mc.renderGlobal.loadRenderers();
    }

    public void initXray() {
        xrayBlocks = new ArrayList<>();

        if (diamond.getValue()) {
            xrayBlocks.add(Blocks.DIAMOND_ORE);
        } else {
            xrayBlocks.remove(Blocks.DIAMOND_ORE);
        }
        if (coal.getValue()) {
            xrayBlocks.add(Blocks.COAL_ORE);
        } else {
            xrayBlocks.remove(Blocks.COAL_ORE);
        }
        if (gold.getValue()) {
            xrayBlocks.add(Blocks.GOLD_ORE);
        } else {
            xrayBlocks.remove(Blocks.GOLD_ORE);
        }
        if (iron.getValue()) {
            xrayBlocks.add(Blocks.IRON_ORE);
        } else {
            xrayBlocks.remove(Blocks.IRON_ORE);
        }
        if (lapiz.getValue()) {
            xrayBlocks.add(Blocks.LAPIS_ORE);
        } else {
            xrayBlocks.remove(Blocks.LAPIS_ORE);
        }
        if (redstone.getValue()) {
            xrayBlocks.add(Blocks.REDSTONE_ORE);
            xrayBlocks.add(Blocks.LIT_REDSTONE_ORE);
        } else {
            xrayBlocks.remove(Blocks.REDSTONE_ORE);
            xrayBlocks.remove(Blocks.LIT_REDSTONE_ORE);
        }
        if (quartz.getValue()) {
            xrayBlocks.add(Blocks.QUARTZ_ORE);
        } else {
            xrayBlocks.remove(Blocks.QUARTZ_ORE);
        }
        if (water.getValue()) {
            xrayBlocks.add(Blocks.WATER);
            xrayBlocks.add(Blocks.FLOWING_WATER);
        } else {
            xrayBlocks.remove(Blocks.WATER);
            xrayBlocks.remove(Blocks.FLOWING_WATER);
        }
        if (lava.getValue()) {
            xrayBlocks.add(Blocks.LAVA);
            xrayBlocks.add(Blocks.FLOWING_LAVA);
        } else {
            xrayBlocks.remove(Blocks.LAVA);
            xrayBlocks.remove(Blocks.FLOWING_LAVA);
        }
        if (obsidian.getValue()) {
            xrayBlocks.add(Blocks.OBSIDIAN);
        } else {
            xrayBlocks.remove(Blocks.OBSIDIAN);
        }
    }
}
