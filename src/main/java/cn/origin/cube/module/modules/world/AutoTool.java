package cn.origin.cube.module.modules.world;

import cn.origin.cube.module.Module;
import cn.origin.cube.module.modules.combat.KillAura;
import cn.origin.cube.core.settings.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AutoTool extends Module {

    BooleanSetting silent = registerSetting("Silent", false);
    BooleanSetting render = registerSetting("Render", false);

    final int oldSlot = KillAura.mc.player.inventory.currentItem;

    public Block[] pickaxeBlocks = {Blocks.OBSIDIAN, Blocks.STONE, Blocks.COBBLESTONE};
    public BlockPos currentBlock = null;

    @SubscribeEvent
    public void onDamageEvent(){

    }
}
