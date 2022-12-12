package dev.canon.cue.module.modules.world;

import dev.canon.cue.event.events.world.PlayerDamageBlockEvent;
import dev.canon.cue.event.events.world.Render3DEvent;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import dev.canon.cue.module.modules.client.ClickGui;
import dev.canon.cue.settings.BooleanSetting;
import dev.canon.cue.utils.render.Render3DUtil;
import dev.canon.cue.module.Category;
import dev.canon.cue.module.Module;
import dev.canon.cue.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleInfo(name = "PacketMine",
        descriptions = "PacketMine",
        category = Category.WORLD)
public class PacketMine extends Module {
    public BooleanSetting antiNeededBlocks = registerSetting("AntiNeededBlocks", true);
    public BooleanSetting cancel = registerSetting("Cancel", true);

    public Block[] neededBlocks = {Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST, Blocks.CHEST};
    public BlockPos currentBlock = null;

    @SubscribeEvent
    public void onDamageBlock(PlayerDamageBlockEvent event) {
        for (Block block : neededBlocks) {
            if (!antiNeededBlocks.getValue()) break;
            if (mc.world.getBlockState(event.getPos()).getBlock().equals(block)) {
                return;
            }
        }
        if (cancel.getValue()) event.setCanceled(true);
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFacing()));
        mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFacing()));
        currentBlock = event.getPos();
    }

    @Override
    public void onUpdate() {
        if (fullNullCheck()) return;
        if (currentBlock == null) return;
        if (mc.world.getBlockState(currentBlock).getBlock().equals(Blocks.AIR)) currentBlock = null;
    }

    @Override
    public void onRender3D(Render3DEvent event) {
        if (fullNullCheck()) return;
        if (currentBlock != null) {
            Render3DUtil.drawBlockBox(currentBlock, ClickGui.getCurrentColor(), true, 3);
        }
    }
}
