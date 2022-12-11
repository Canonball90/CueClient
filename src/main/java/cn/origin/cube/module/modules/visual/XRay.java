package cn.origin.cube.module.modules.visual;

import cn.origin.cube.module.Category;
import cn.origin.cube.module.Module;
import cn.origin.cube.module.ModuleInfo;
import cn.origin.cube.module.modules.client.ClickGui;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.util.ArrayList;

@ModuleInfo(name = "XRay", descriptions = "Always light", category = Category.VISUAL)
public class XRay extends Module {
    public static ArrayList<Block> xrayBlocks;
    public static XRay INSTANCE;

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

        xrayBlocks.add(Blocks.DIAMOND_ORE);
        xrayBlocks.add(Blocks.COAL_ORE);
        xrayBlocks.add(Blocks.GOLD_ORE);
        xrayBlocks.add(Blocks.IRON_ORE);
        xrayBlocks.add(Blocks.LAPIS_ORE);
        xrayBlocks.add(Blocks.REDSTONE_ORE);
        xrayBlocks.add(Blocks.LIT_REDSTONE_ORE);
        xrayBlocks.add(Blocks.QUARTZ_ORE);
        xrayBlocks.add(Blocks.WATER);
        xrayBlocks.add(Blocks.FLOWING_WATER);
        xrayBlocks.add(Blocks.LAVA);
        xrayBlocks.add(Blocks.FLOWING_LAVA);
    }
}
